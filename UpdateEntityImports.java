public class UpdateEntityImports {
    public static void main(String[] args) {
        System.out.println("// Replace the imports in Checkin.java");
        System.out.println("package com.hotel.erp.entity;");
        System.out.println("");
        System.out.println("import java.time.LocalDate;");
        System.out.println("import java.time.LocalDateTime;");
        System.out.println("import jakarta.persistence.*;");
        System.out.println("");
        System.out.println("@Entity");
        System.out.println("@Table(name = \"checkin\")");
        System.out.println("public class Checkin {");
        System.out.println("    @Id");
        System.out.println("    @GeneratedValue(strategy = GenerationType.IDENTITY)");
        System.out.println("    @Column(name = \"checkin_id\")");
        System.out.println("    private Long id;");
        System.out.println("");
        System.out.println("    // Copy from here and update the file manually");
    }
}
