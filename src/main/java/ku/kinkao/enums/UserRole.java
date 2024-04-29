package ku.kinkao.enums;

public enum UserRole {
   USER("user"),
    ADMIN("admin");

    public final String label;

    private UserRole(String label) {
        this.label = label;
    }
}
