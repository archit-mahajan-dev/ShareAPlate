package in.shareaplate.ui;

import in.shareaplate.dao.DonationDAO;
import in.shareaplate.model.Donation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MainFrame extends JFrame {
    private static final Color NAVY = new Color(24, 48, 39);
    private static final Color GREEN = new Color(42, 119, 82);
    private static final Color GREEN_LIGHT = new Color(230, 243, 232);
    private static final Color BG = new Color(247, 249, 245);
    private static final Color TEXT = new Color(39, 55, 46);
    private static final Color MUTED = new Color(112, 128, 117);
    private static final Color BORDER = new Color(226, 233, 225);
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final DonationDAO dao = new DonationDAO();
    private final JTextField search = new JTextField(20);
    private final JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All statuses","Available","Reserved","Collected"});
    private final JComboBox<String> typeFilter = new JComboBox<>(new String[]{"All food types","Cooked meals","Packaged food","Fresh produce","Bakery","Other"});
    private final DefaultTableModel tableModel = new DefaultTableModel(
        new String[]{"ID","Food item","Category","Portions","Available until","Donor","Contact","Pickup location","Status","Notes"}, 0) {
        @Override public boolean isCellEditable(int row, int column) { return false; }
    };
    private final JTable table = new JTable(tableModel);
    private final JLabel totalValue = new JLabel("0"), availableValue = new JLabel("0"),
        collectedValue = new JLabel("0"), portionsValue = new JLabel("0");
    private JLabel connectionLabel;

    public MainFrame() {
        setTitle("ShareAPlate — Food Donation Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));
        setSize(1380, 820);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());
        add(buildSidebar(), BorderLayout.WEST);
        add(buildWorkspace(), BorderLayout.CENTER);
        styleTable();
        refresh();
    }

    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(230, 100));
        side.setBackground(NAVY);
        side.setBorder(new EmptyBorder(24, 18, 20, 18));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        JLabel brandIcon = new JLabel("  ♡  ");
        brandIcon.setOpaque(true);
        brandIcon.setBackground(new Color(220, 239, 221));
        brandIcon.setForeground(GREEN);
        brandIcon.setFont(new Font("SansSerif", Font.BOLD, 22));
        brandIcon.setBorder(new EmptyBorder(8, 5, 8, 5));
        brandIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel brand = new JLabel("ShareAPlate");
        brand.setFont(new Font("SansSerif", Font.BOLD, 21));
        brand.setForeground(Color.WHITE);
        brand.setBorder(new EmptyBorder(14, 0, 3, 0));
        brand.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("FOOD • COMMUNITY • CARE");
        tagline.setFont(new Font("SansSerif", Font.BOLD, 9));
        tagline.setForeground(new Color(174, 202, 180));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        side.add(brandIcon);
        side.add(brand);
        side.add(tagline);
        side.add(Box.createVerticalStrut(42));
        side.add(navItem("▦", "Overview", true));
        side.add(Box.createVerticalStrut(8));
        side.add(navItem("＋", "Donation listings", false));
        side.add(Box.createVerticalStrut(8));
        side.add(navItem("↻", "Collection tracking", false));
        side.add(Box.createVerticalStrut(34));

        JLabel community = new JLabel("COMMUNITY PROJECT");
        community.setFont(new Font("SansSerif", Font.BOLD, 9));
        community.setForeground(new Color(145, 177, 151));
        community.setAlignmentX(Component.LEFT_ALIGNMENT);
        side.add(community);
        side.add(Box.createVerticalStrut(10));

        JTextArea purpose = new JTextArea("Helping community groups coordinate surplus food and reduce avoidable waste.");
        purpose.setWrapStyleWord(true);
        purpose.setLineWrap(true);
        purpose.setEditable(false);
        purpose.setFocusable(false);
        purpose.setOpaque(false);
        purpose.setForeground(new Color(209, 224, 211));
        purpose.setFont(new Font("SansSerif", Font.PLAIN, 12));
        purpose.setAlignmentX(Component.LEFT_ALIGNMENT);
        purpose.setMaximumSize(new Dimension(195, 90));
        side.add(purpose);

        side.add(Box.createVerticalGlue());
        JPanel lead = new JPanel();
        lead.setOpaque(false);
        lead.setLayout(new BoxLayout(lead, BoxLayout.Y_AXIS));
        lead.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel leadLabel = new JLabel("PROJECT LEAD");
        leadLabel.setFont(new Font("SansSerif", Font.BOLD, 9));
        leadLabel.setForeground(new Color(145, 177, 151));
        JLabel leadName = new JLabel("Archit Mahajan");
        leadName.setFont(new Font("SansSerif", Font.BOLD, 13));
        leadName.setForeground(Color.WHITE);
        JLabel version = new JLabel("Student prototype  •  v1.0");
        version.setFont(new Font("SansSerif", Font.PLAIN, 10));
        version.setForeground(new Color(174, 202, 180));
        lead.add(leadLabel); lead.add(Box.createVerticalStrut(6)); lead.add(leadName);
        lead.add(Box.createVerticalStrut(4)); lead.add(version);
        side.add(lead);
        return side;
    }

    private JPanel navItem(String icon, String label, boolean active) {
        JPanel item = new JPanel(new BorderLayout(10, 0));
        item.setMaximumSize(new Dimension(195, 42));
        item.setPreferredSize(new Dimension(195, 42));
        item.setBorder(new EmptyBorder(10, 12, 10, 12));
        item.setBackground(active ? new Color(54, 105, 75) : NAVY);
        JLabel glyph = new JLabel(icon);
        glyph.setForeground(active ? Color.WHITE : new Color(174, 202, 180));
        glyph.setFont(new Font("SansSerif", Font.BOLD, 16));
        JLabel text = new JLabel(label);
        text.setForeground(active ? Color.WHITE : new Color(207, 222, 209));
        text.setFont(new Font("SansSerif", active ? Font.BOLD : Font.PLAIN, 12));
        item.add(glyph, BorderLayout.WEST); item.add(text, BorderLayout.CENTER);
        item.setAlignmentX(Component.LEFT_ALIGNMENT);
        return item;
    }

    private JPanel buildWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout(0, 20));
        workspace.setBackground(BG);
        workspace.setBorder(new EmptyBorder(26, 28, 22, 28));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JPanel headings = new JPanel();
        headings.setOpaque(false);
        headings.setLayout(new BoxLayout(headings, BoxLayout.Y_AXIS));
        JLabel eyebrow = new JLabel("COMMUNITY DASHBOARD");
        eyebrow.setFont(new Font("SansSerif", Font.BOLD, 10));
        eyebrow.setForeground(GREEN);
        JLabel title = new JLabel("Good things start with sharing.");
        title.setFont(new Font("SansSerif", Font.BOLD, 27));
        title.setForeground(TEXT);
        title.setBorder(new EmptyBorder(7, 0, 5, 0));
        JLabel subtitle = new JLabel("Manage food listings and keep every collection moving.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(MUTED);
        headings.add(eyebrow); headings.add(title); headings.add(subtitle);
        top.add(headings, BorderLayout.WEST);

        JButton addButton = button("+  Add donation", GREEN, Color.WHITE);
        addButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        addButton.addActionListener(e -> showDonationDialog(null));
        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        topRight.setOpaque(false);
        topRight.add(addButton);
        top.add(topRight, BorderLayout.EAST);
        workspace.add(top, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(0, 16));
        center.setOpaque(false);
        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 0));
        stats.setOpaque(false);
        stats.add(statCard("TOTAL LISTINGS", totalValue, "All donations", new Color(235, 241, 232)));
        stats.add(statCard("AVAILABLE NOW", availableValue, "Ready for pickup", new Color(226, 243, 231)));
        stats.add(statCard("COLLECTED", collectedValue, "Completed handovers", new Color(238, 234, 248)));
        stats.add(statCard("PORTIONS LISTED", portionsValue, "Food portions / items", new Color(251, 241, 220)));
        center.add(stats, BorderLayout.NORTH);

        JPanel listingPanel = new JPanel(new BorderLayout(0, 12));
        listingPanel.setBackground(Color.WHITE);
        listingPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(18, 18, 14, 18)
        ));

        JPanel listingHeader = new JPanel(new BorderLayout());
        listingHeader.setOpaque(false);
        JPanel labels = new JPanel(); labels.setOpaque(false);
        labels.setLayout(new BoxLayout(labels, BoxLayout.Y_AXIS));
        JLabel listTitle = new JLabel("Donation listings");
        listTitle.setFont(new Font("SansSerif", Font.BOLD, 17)); listTitle.setForeground(TEXT);
        JLabel listSubtitle = new JLabel("Search, filter, and update donation status.");
        listSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 11)); listSubtitle.setForeground(MUTED);
        listSubtitle.setBorder(new EmptyBorder(4,0,0,0));
        labels.add(listTitle); labels.add(listSubtitle);
        connectionLabel = new JLabel("●  Database connected");
        connectionLabel.setForeground(GREEN); connectionLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        listingHeader.add(labels, BorderLayout.WEST); listingHeader.add(connectionLabel, BorderLayout.EAST);
        listingPanel.add(listingHeader, BorderLayout.NORTH);

        JPanel filters = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        filters.setOpaque(false);
        search.setPreferredSize(new Dimension(260, 34));
        search.setFont(new Font("SansSerif", Font.PLAIN, 12));
        search.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER),
            new EmptyBorder(6, 10, 6, 10)));
        statusFilter.setPreferredSize(new Dimension(145, 34));
        typeFilter.setPreferredSize(new Dimension(160, 34));
        styleCombo(statusFilter); styleCombo(typeFilter);
        JButton searchBtn = button("Search", GREEN, Color.WHITE);
        JButton clearBtn = button("Reset", new Color(239, 243, 237), TEXT);
        filters.add(search); filters.add(searchBtn); filters.add(statusFilter); filters.add(typeFilter); filters.add(clearBtn);

        table.setRowHeight(34);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.getViewport().setBackground(Color.WHITE);

        JPanel lower = new JPanel(new BorderLayout());
        lower.setOpaque(false);
        JLabel hint = new JLabel("Tip: select a row to edit or update its collection status.");
        hint.setFont(new Font("SansSerif", Font.PLAIN, 10)); hint.setForeground(MUTED);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 7, 0)); actions.setOpaque(false);
        JButton edit = button("Edit", new Color(239,243,237), TEXT);
        JButton reserve = button("Mark reserved", new Color(255,243,218), new Color(131,91,22));
        JButton collect = button("Mark collected", GREEN_LIGHT, GREEN);
        JButton delete = button("Delete", new Color(251,232,228), new Color(157,66,55));
        JButton refreshBtn = button("↻", Color.WHITE, TEXT);
        actions.add(edit); actions.add(reserve); actions.add(collect); actions.add(delete); actions.add(refreshBtn);
        lower.add(hint, BorderLayout.WEST); lower.add(actions, BorderLayout.EAST);

        JPanel middle = new JPanel(new BorderLayout(0, 12)); middle.setOpaque(false);
        middle.add(filters, BorderLayout.NORTH); middle.add(scroll, BorderLayout.CENTER);
        listingPanel.add(middle, BorderLayout.CENTER);
        listingPanel.add(lower, BorderLayout.SOUTH);
        center.add(listingPanel, BorderLayout.CENTER);
        workspace.add(center, BorderLayout.CENTER);

        searchBtn.addActionListener(e -> refresh());
        search.addActionListener(e -> refresh());
        clearBtn.addActionListener(e -> { search.setText(""); statusFilter.setSelectedIndex(0); typeFilter.setSelectedIndex(0); refresh(); });
        statusFilter.addActionListener(e -> refresh()); typeFilter.addActionListener(e -> refresh());
        edit.addActionListener(e -> { Donation d = selectedDonation(); if (d != null) showDonationDialog(d); });
        reserve.addActionListener(e -> changeStatus("Reserved"));
        collect.addActionListener(e -> changeStatus("Collected"));
        delete.addActionListener(e -> deleteSelected());
        refreshBtn.addActionListener(e -> refresh());
        return workspace;
    }

    private JPanel statCard(String label, JLabel value, String caption, Color tint) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER), new EmptyBorder(15, 16, 13, 16)));
        JLabel name = new JLabel(label); name.setFont(new Font("SansSerif", Font.BOLD, 9)); name.setForeground(MUTED);
        value.setFont(new Font("SansSerif", Font.BOLD, 27)); value.setForeground(TEXT);
        JLabel sub = new JLabel(caption); sub.setFont(new Font("SansSerif", Font.PLAIN, 10)); sub.setForeground(MUTED);
        JPanel marker = new JPanel(); marker.setBackground(tint); marker.setPreferredSize(new Dimension(4, 1));
        card.add(name, BorderLayout.NORTH); card.add(value, BorderLayout.CENTER); card.add(sub, BorderLayout.SOUTH);
        card.add(marker, BorderLayout.WEST);
        return card;
    }

    private void styleCombo(JComboBox<String> combo) {
        combo.setBackground(Color.WHITE);
        combo.setForeground(TEXT);
        combo.setFont(new Font("SansSerif", Font.PLAIN, 11));
    }

    private void styleTable() {
        table.getTableHeader().setBackground(new Color(240, 245, 238));
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setPreferredSize(new Dimension(10, 36));
        table.setFont(new Font("SansSerif", Font.PLAIN, 11));
        table.setForeground(TEXT);
        table.setGridColor(new Color(241, 244, 239));
        table.setSelectionBackground(new Color(222, 239, 224));
        table.setSelectionForeground(TEXT);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        left.setBorder(new EmptyBorder(0, 8, 0, 8));
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(left);
        table.getColumnModel().getColumn(0).setPreferredWidth(40);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(170);
        table.getColumnModel().getColumn(9).setPreferredWidth(180);
    }

    private JButton button(String text, Color background, Color foreground) {
        JButton b = new JButton(text);
        b.setBackground(background); b.setForeground(foreground);
        b.setFocusPainted(false); b.setFont(new Font("SansSerif", Font.BOLD, 11));
        b.setBorder(new EmptyBorder(9, 12, 9, 12));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void refresh() {
        try {
            List<Donation> donations = dao.findAll(search.getText(), (String)statusFilter.getSelectedItem(), (String)typeFilter.getSelectedItem());
            tableModel.setRowCount(0);
            for (Donation d : donations) {
                tableModel.addRow(new Object[]{d.getId(),d.getFoodName(),d.getFoodType(),d.getQuantity(),
                    d.getAvailableUntil().format(INPUT_FORMAT),d.getDonorName(),d.getContactDetails(),
                    d.getPickupLocation(),d.getStatus(),d.getNotes()});
            }
            totalValue.setText(String.valueOf(dao.countAll()));
            availableValue.setText(String.valueOf(dao.countStatus("Available")));
            collectedValue.setText(String.valueOf(dao.countStatus("Collected")));
            portionsValue.setText(String.valueOf(dao.totalQuantity()));
            if (connectionLabel != null) {
                connectionLabel.setText("●  Database connected");
                connectionLabel.setForeground(GREEN);
            }
        } catch (SQLException ex) {
            if (connectionLabel != null) {
                connectionLabel.setText("●  Database unavailable");
                connectionLabel.setForeground(new Color(173, 79, 63));
            }
            showError("Could not load donations", ex);
        }
    }

    private Donation selectedDonation() {
        int viewRow = table.getSelectedRow();
        if (viewRow < 0) { JOptionPane.showMessageDialog(this, "Please select a donation row first."); return null; }
        int row = table.convertRowIndexToModel(viewRow);
        long id = ((Number)tableModel.getValueAt(row,0)).longValue();
        try {
            List<Donation> all = dao.findAll("", "All statuses", "All food types");
            return all.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
        } catch (SQLException ex) { showError("Could not read selected donation", ex); return null; }
    }

    private void showDonationDialog(Donation existing) {
        JTextField food = new JTextField(existing == null ? "" : existing.getFoodName(), 22);
        JComboBox<String> type = new JComboBox<>(new String[]{"Cooked meals","Packaged food","Fresh produce","Bakery","Other"});
        if (existing != null) type.setSelectedItem(existing.getFoodType());
        JSpinner quantity = new JSpinner(new SpinnerNumberModel(existing == null ? 1 : existing.getQuantity(),1,100000,1));
        JTextField until = new JTextField(existing == null ? LocalDateTime.now().plusHours(4).format(INPUT_FORMAT) : existing.getAvailableUntil().format(INPUT_FORMAT), 22);
        JTextField donor = new JTextField(existing == null ? "" : existing.getDonorName(), 22);
        JTextField contact = new JTextField(existing == null ? "" : existing.getContactDetails(), 22);
        JTextField location = new JTextField(existing == null ? "" : existing.getPickupLocation(), 22);
        JTextArea notes = new JTextArea(existing == null ? "" : safe(existing.getNotes()), 3, 22); notes.setLineWrap(true); notes.setWrapStyleWord(true);
        JComboBox<String> status = new JComboBox<>(new String[]{"Available","Reserved","Collected"});
        if (existing != null) status.setSelectedItem(existing.getStatus());
        JPanel form = new JPanel(new GridBagLayout()); form.setBorder(new EmptyBorder(8,8,8,8));
        GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(6,6,6,6); g.fill = GridBagConstraints.HORIZONTAL;
        int row = 0;
        addField(form,g,row++,"Food / item name *",food); addField(form,g,row++,"Food category *",type);
        addField(form,g,row++,"Quantity / portions *",quantity); addField(form,g,row++,"Available until (yyyy-MM-dd HH:mm) *",until);
        addField(form,g,row++,"Donor / organization *",donor); addField(form,g,row++,"Contact details *",contact);
        addField(form,g,row++,"Pickup location *",location); addField(form,g,row++,"Status",status);
        addField(form,g,row++,"Storage / allergens / notes",new JScrollPane(notes));
        int answer = JOptionPane.showConfirmDialog(this, form, existing == null ? "Add a food donation" : "Edit donation",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer != JOptionPane.OK_OPTION) return;
        if (food.getText().isBlank() || donor.getText().isBlank() || contact.getText().isBlank() || location.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Please fill in all required text fields.", "Validation", JOptionPane.WARNING_MESSAGE); return;
        }
        try {
            LocalDateTime deadline = LocalDateTime.parse(until.getText().trim(), INPUT_FORMAT);
            if (!deadline.isAfter(LocalDateTime.now())) {
                JOptionPane.showMessageDialog(this, "Available-until time must be in the future."); return;
            }
            Donation d = new Donation(existing == null ? 0 : existing.getId(), food.getText().trim(),
                (String)type.getSelectedItem(), (Integer)quantity.getValue(), deadline, donor.getText().trim(),
                contact.getText().trim(), location.getText().trim(), notes.getText().trim(), (String)status.getSelectedItem());
            if (existing == null) dao.insert(d); else dao.update(d);
            refresh();
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Use the date/time format yyyy-MM-dd HH:mm, e.g. 2026-10-18 18:30.");
        } catch (SQLException ex) { showError("Could not save donation", ex); }
    }

    private void addField(JPanel panel, GridBagConstraints g, int row, String label, Component field) {
        g.gridx = 0; g.gridy = row; g.weightx = 0; panel.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1; panel.add(field, g);
    }

    private void changeStatus(String status) {
        Donation d = selectedDonation(); if (d == null) return;
        try { dao.updateStatus(d.getId(), status); refresh(); }
        catch (SQLException ex) { showError("Could not update status", ex); }
    }

    private void deleteSelected() {
        Donation d = selectedDonation(); if (d == null) return;
        int confirm = JOptionPane.showConfirmDialog(this, "Delete donation \"" + d.getFoodName() + "\"?", "Confirm deletion", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;
        try { dao.delete(d.getId()); refresh(); }
        catch (SQLException ex) { showError("Could not delete donation", ex); }
    }

    private void showError(String title, Exception ex) {
        JOptionPane.showMessageDialog(this, title + ".\n" + ex.getMessage(), "ShareAPlate", JOptionPane.ERROR_MESSAGE);
    }
    private String safe(String value) { return value == null ? "" : value; }
}
