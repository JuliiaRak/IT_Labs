package dbmanager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.border.AbstractBorder;
import java.awt.*;

public class ButtonsManager {

    public static JButton GetOpenDatabaseButton() {
        JButton OpenDatabaseButton = new JButton("Open Database");
        OpenDatabaseButton.setPreferredSize(new Dimension(170, 55));

        // заокруглення кнопочки
        OpenDatabaseButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        Font buttonFont = new Font("Times New Roman", Font.BOLD, 17);
        OpenDatabaseButton.setFont(buttonFont);

        OpenDatabaseButton.setOpaque(false);
        OpenDatabaseButton.setBorderPainted(false);
        OpenDatabaseButton.setFocusPainted(false);
        OpenDatabaseButton.setContentAreaFilled(false);
//        OpenDatabaseButton.setForeground(Color.WHITE);

        OpenDatabaseButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(20, 90, 160), 0, button.getHeight(), new Color(20, 70, 140));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(100, 209, 255), 0, button.getHeight(), new Color(100, 209, 255));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(50, 130, 240), 0, button.getHeight(), new Color(20, 90, 200));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return OpenDatabaseButton;
    }

    public static JButton getAddRowButton() {
        JButton CreateDBTableButton = new JButton("Add Row");
        CreateDBTableButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        CreateDBTableButton.setFont(buttonFont);
        CreateDBTableButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        CreateDBTableButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(240, 85, 140), 0, button.getHeight(), new Color(220, 65, 120));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(200, 105, 170), 0, button.getHeight(), new Color(200, 105, 170));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(250, 95, 255), 0, button.getHeight(), new Color(230, 75, 255));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return CreateDBTableButton;
    }
    public static JButton getCreateDBTableButton() {
        JButton CreateDBTableButton = new JButton("Create DB Table");
        CreateDBTableButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        CreateDBTableButton.setFont(buttonFont);
        CreateDBTableButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        CreateDBTableButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(240, 85, 140), 0, button.getHeight(), new Color(220, 65, 120));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(200, 105, 170), 0, button.getHeight(), new Color(200, 105, 170));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(250, 95, 255), 0, button.getHeight(), new Color(230, 75, 255));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return CreateDBTableButton;
    }

    public static JButton getCreateDBButton() {
        JButton CreateDBButton = new JButton("Create Database");
        CreateDBButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        CreateDBButton.setFont(buttonFont);
        CreateDBButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        CreateDBButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(20, 90, 160), 0, button.getHeight(), new Color(20, 70, 140));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(100, 209, 255), 0, button.getHeight(), new Color(100, 209, 255));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(50, 130, 240), 0, button.getHeight(), new Color(20, 90, 200));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return CreateDBButton;
    }

    public static JButton getRemoveDBTableButton() {
        JButton RemoveDBTableButton = new JButton("Remove DB table");
        RemoveDBTableButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        RemoveDBTableButton.setFont(buttonFont);
        RemoveDBTableButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        RemoveDBTableButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(240, 85, 140), 0, button.getHeight(), new Color(220, 65, 120));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(200, 105, 170), 0, button.getHeight(), new Color(200, 105, 170));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(250, 95, 255), 0, button.getHeight(), new Color(230, 75, 255));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return RemoveDBTableButton;
    }

    public static JButton getTableRenameButton() {
        JButton renameRearrangeButton = new JButton("Rename Column");
        renameRearrangeButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        renameRearrangeButton.setFont(buttonFont);
        renameRearrangeButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        renameRearrangeButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(240, 85, 140), 0, button.getHeight(), new Color(220, 65, 120));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(200, 105, 170), 0, button.getHeight(), new Color(200, 105, 170));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(250, 95, 255), 0, button.getHeight(), new Color(230, 75, 255));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return renameRearrangeButton;
    }

    public static JButton getTableRearrangeButton() {
        JButton renameRearrangeButton = new JButton("Rearrange Column");
        renameRearrangeButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        renameRearrangeButton.setFont(buttonFont);
        renameRearrangeButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        renameRearrangeButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(240, 85, 140), 0, button.getHeight(), new Color(220, 65, 120));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(200, 105, 170), 0, button.getHeight(), new Color(200, 105, 170));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(250, 95, 255), 0, button.getHeight(), new Color(230, 75, 255));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return renameRearrangeButton;
    }
    public static JButton getSaveDatabaseAsButton() {
        JButton SaveDatabaseAsButton = new JButton("Save DB as...");
        SaveDatabaseAsButton.setPreferredSize(new Dimension(170, 55));
        Font buttonFont = new Font("Arial", Font.BOLD, 17);
        SaveDatabaseAsButton.setFont(buttonFont);
        SaveDatabaseAsButton.setBorder(new AbstractBorder() {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawRoundRect(x, y, width - 1, height - 1, 25, 25);
                g2d.dispose();
            }
        });

        SaveDatabaseAsButton.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                JButton button = (JButton) c;
                Graphics2D g2d = (Graphics2D) g.create();

                // Визначаємо градієнт в залежності від стану кнопки
                GradientPaint gradientPaint;
                if (button.getModel().isPressed()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(20, 90, 160), 0, button.getHeight(), new Color(20, 70, 140));
                } else if (button.getModel().isRollover()) {
                    gradientPaint = new GradientPaint(0, 0, new Color(100, 209, 255), 0, button.getHeight(), new Color(100, 209, 255));
                } else {
                    gradientPaint = new GradientPaint(0, 0, new Color(50, 130, 240), 0, button.getHeight(), new Color(20, 90, 200));
                }

                g2d.setPaint(gradientPaint);
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight(), 25, 25);

                // Відблиск
                g2d.setColor(new Color(255, 255, 255, 100));
                g2d.fillRoundRect(0, 0, button.getWidth(), button.getHeight() / 2, 25, 25);

                // Тінь
                g2d.setColor(new Color(0, 0, 0, 50));
                g2d.drawRoundRect(0, 0, button.getWidth() - 1, button.getHeight() - 1, 25, 25);

                g2d.dispose();

                super.paint(g, c);
            }
        });

        return SaveDatabaseAsButton;
    }

    // і так далі для кожної кнопки...
}