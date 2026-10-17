package in.shareaplate.dao;

import in.shareaplate.db.Database;
import in.shareaplate.model.Donation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DonationDAO {
    private static final String COLUMNS =
        "id, food_name, food_type, quantity, available_until, donor_name, contact_details, pickup_location, notes, status";

    public List<Donation> findAll(String search, String status, String type) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT " + COLUMNS + " FROM donations WHERE 1=1");
        List<Object> args = new ArrayList<>();
        if (search != null && !search.isBlank()) {
            sql.append(" AND (food_name LIKE ? OR donor_name LIKE ? OR pickup_location LIKE ?)");
            String term = "%" + search.trim() + "%";
            args.add(term); args.add(term); args.add(term);
        }
        if (status != null && !status.equals("All statuses")) {
            sql.append(" AND status = ?"); args.add(status);
        }
        if (type != null && !type.equals("All food types")) {
            sql.append(" AND food_type = ?"); args.add(type);
        }
        sql.append(" ORDER BY created_at DESC");
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql.toString())) {
            for (int i = 0; i < args.size(); i++) ps.setObject(i + 1, args.get(i));
            try (ResultSet rs = ps.executeQuery()) {
                List<Donation> result = new ArrayList<>();
                while (rs.next()) result.add(map(rs));
                return result;
            }
        }
    }

    public void insert(Donation d) throws SQLException {
        String sql = "INSERT INTO donations (food_name, food_type, quantity, available_until, donor_name, contact_details, pickup_location, notes, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, d); ps.executeUpdate();
        }
    }

    public void update(Donation d) throws SQLException {
        String sql = "UPDATE donations SET food_name=?, food_type=?, quantity=?, available_until=?, donor_name=?, contact_details=?, pickup_location=?, notes=?, status=? WHERE id=?";
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            bind(ps, d); ps.setLong(10, d.getId());
            if (ps.executeUpdate() == 0) throw new SQLException("Donation no longer exists.");
        }
    }

    public void updateStatus(long id, String status) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("UPDATE donations SET status=? WHERE id=?")) {
            ps.setString(1, status); ps.setLong(2, id);
            if (ps.executeUpdate() == 0) throw new SQLException("Donation no longer exists.");
        }
    }

    public void delete(long id) throws SQLException {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM donations WHERE id=?")) {
            ps.setLong(1, id);
            if (ps.executeUpdate() == 0) throw new SQLException("Donation no longer exists.");
        }
    }

    public int countAll() throws SQLException { return count("SELECT COUNT(*) FROM donations"); }
    public int countStatus(String status) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM donations WHERE status=?")) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
        }
    }
    public long totalQuantity() throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT COALESCE(SUM(quantity),0) FROM donations");
             ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getLong(1); }
    }
    private int count(String sql) throws SQLException {
        try (Connection c = Database.getConnection(); PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) { rs.next(); return rs.getInt(1); }
    }
    private void bind(PreparedStatement ps, Donation d) throws SQLException {
        ps.setString(1, d.getFoodName()); ps.setString(2, d.getFoodType()); ps.setInt(3, d.getQuantity());
        ps.setTimestamp(4, Timestamp.valueOf(d.getAvailableUntil())); ps.setString(5, d.getDonorName());
        ps.setString(6, d.getContactDetails()); ps.setString(7, d.getPickupLocation());
        ps.setString(8, d.getNotes()); ps.setString(9, d.getStatus());
    }
    private Donation map(ResultSet rs) throws SQLException {
        Timestamp ts = rs.getTimestamp("available_until");
        return new Donation(rs.getLong("id"), rs.getString("food_name"), rs.getString("food_type"),
            rs.getInt("quantity"), ts.toLocalDateTime(), rs.getString("donor_name"),
            rs.getString("contact_details"), rs.getString("pickup_location"), rs.getString("notes"),
            rs.getString("status"));
    }
}
