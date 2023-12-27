package com.vllm.copilot.gui.toolwindows.components;

import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTextArea;
import com.intellij.util.ui.JBUI;
import com.vllm.copilot.gui.toolwindows.components.code.IconJButton;
import com.vllm.copilot.util.DevPilotMessageBundle;
import com.vllm.copilot.DevPilotIcons;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.commons.lang3.StringUtils;

public class UserChatPanel extends JPanel {
    private final JBTextArea textArea;

    private final RoundedPanel iconsPanel;

    private final Consumer<String> sendEvent;

    private final Runnable stopEvent;

    private final JButton button;

    private final AtomicBoolean isSending = new AtomicBoolean(false);

    public UserChatPanel(Consumer<String> sendEvent, Runnable stopEvent) {
        this.sendEvent = sendEvent;
        this.stopEvent = stopEvent;

        textArea = new JBTextArea();
        textArea.setOpaque(false);
        textArea.setLineWrap(true);
        textArea.getEmptyText().setText(DevPilotMessageBundle.get("devpilot.prompt.text.placeholder"));
        textArea.setBorder(JBUI.Borders.empty(8, 4));
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) == 0) {
                        if (isSending()) {
                            e.consume();
                            return;
                        }

                        handleSendEvent();
                        e.consume();
                    } else {
                        // shift + ENTER
                        textArea.append("\n");
                    }
                }
            }
        });
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                Graphics g = UserChatPanel.super.getGraphics();
                if (g == null) {
                    return;
                }
                UserChatPanel.super.paintBorder(g);
            }

            @Override
            public void focusLost(FocusEvent e) {
                Graphics g = UserChatPanel.super.getGraphics();
                if (g == null) {
                    return;
                }
                UserChatPanel.super.paintBorder(g);
            }
        });

        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(JBUI.Borders.compound(
            JBUI.Borders.customLine(JBColor.border(), 0, 0, 0, 0),
            JBUI.Borders.empty(10)));
        this.add(textArea, BorderLayout.CENTER);

        iconsPanel = new RoundedPanel();
        button = new IconJButton(DevPilotIcons.SEND_ICON, DevPilotMessageBundle.get("devpilot.chatWindow.submit"), e -> handleSendEvent());
        iconsPanel.addIconJButton((IconJButton) button);
        this.add(iconsPanel, BorderLayout.EAST);
    }

    public void setIconStop() {
        button.setIcon(DevPilotIcons.STOP_ICON);
        button.setToolTipText(DevPilotMessageBundle.get("devpilot.chatWindow.stop"));
        removeAllActionListener();
        button.addActionListener(e -> handleStopEvent());
    }

    public void setIconSend() {
        button.setIcon(DevPilotIcons.SEND_ICON);
        button.setToolTipText(DevPilotMessageBundle.get("devpilot.chatWindow.submit"));
        removeAllActionListener();
        button.addActionListener(e -> handleSendEvent());
    }

    public boolean isSending() {
        return isSending.get();
    }

    public void setSending(boolean isSending) {
        this.isSending.set(isSending);
    }

    private void handleSendEvent() {
        String message = textArea.getText();

        if (StringUtils.isBlank(message)) {
            return;
        }

        sendEvent.accept(message);
        textArea.setText(null);
    }

    private void handleStopEvent() {
        stopEvent.run();
    }

    private void removeAllActionListener() {
        for (var listener : button.getActionListeners()) {
            button.removeActionListener(listener);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        if (g == null) {
            return;
        }

        Graphics2D g2 = (Graphics2D) g.create();
        if (textArea.isFocusOwner()) {
            g2.setColor(JBUI.CurrentTheme.Focus.focusColor());
            g2.setStroke(new BasicStroke(1.5f));
        } else {
            g2.setColor(JBColor.border());
        }
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
    }

}
