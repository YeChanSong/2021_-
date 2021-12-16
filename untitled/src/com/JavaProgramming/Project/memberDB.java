package com.JavaProgramming.Project;

import java.util.*;
import java.sql.*;

interface DBString {
    Scanner scan = new Scanner(System.in);
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://127.0.0.1:3306/testdb?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false";
    String id = "root", pw="1234";
}

interface MemberSQL {
    String selectAllSQL = "select * from member";
    String selectNameSQL = "select * from member where name=?";
    String insertMemberSQL = "insert into member(name,email,major) values (?,?,?)";
    String updateMemberSQL = "update member set email=?, major=? where name=?";
    String deleteMemberSQL = "delete from member where email=?";
    String selectEmailSQL = "select * from member where email=?";
}


class MemberDBMgmt {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public void DBConnection() throws ClassNotFoundException, SQLException {
        Class.forName(DBString.driver);
        conn = DriverManager.getConnection(DBString.url, DBString.id, DBString.pw);
    }
    public void selectDisplay(ResultSet rs) throws SQLException {
        System.out.println("---------------------------------------------------");
        System.out.println(String.format("%-9s %-10s %-20s %-10s", "번호","이름","이메일","전공"));
        System.out.println("---------------------------------------------------");

        while(rs.next()) {
            System.out.println(String.format("%-9s %-10s %-20s %-10s", rs.getString("id"), rs.getString("name"), rs.getString("email"), rs.getString("major")));
        }
    }

    public void memberAdd() throws ClassNotFoundException, SQLException {
        System.out.println("\n=== 등록할 회원정보 ===");
        System.out.print("이름>> ");
        String name = DBString.scan.next();
        System.out.print("이메일>> ");
        String email = DBString.scan.next();
        System.out.print("전공>> ");
        String major = DBString.scan.next();

        DBConnection();

        pstmt = conn.prepareStatement(MemberSQL.insertMemberSQL);
        pstmt.setString(1, name);
        pstmt.setString(2, email);
        pstmt.setString(3, major);
        pstmt.executeUpdate();
        System.out.printf("%s %s %s 등록됨\n", name, email, major);
    }

    public void memberSelectWithEmail() throws ClassNotFoundException, SQLException {
        System.out.println("\n==== 조회할 회원의 이메일 ====");
        System.out.print("\n이메일>> ");
        String email = DBString.scan.next();

        DBConnection();

        pstmt = conn.prepareStatement(MemberSQL.selectEmailSQL);
        pstmt.setString(1, email);
        rs = pstmt.executeQuery();
        selectDisplay(rs);
    }

    public void memberDeleteWithEmail() throws ClassNotFoundException, SQLException{
        System.out.println("\n==== 삭제할 회원의 이메일 ====");
        System.out.print("\n이메일>> ");
        String email = DBString.scan.next();

        DBConnection();

        pstmt = conn.prepareStatement(MemberSQL.deleteMemberSQL);
        pstmt.setString(1, email);
        pstmt.executeUpdate();

        System.out.printf("Account with Email: %s is deleted..\n", email);
    }

    public void memberSelectAll() throws ClassNotFoundException, SQLException {
        DBConnection();
        pstmt = conn.prepareStatement(MemberSQL.selectAllSQL);
        rs = pstmt.executeQuery();
        selectDisplay(rs);
    }
    public void updateMember(String name) throws SQLException {
        System.out.print("\n이메일>> ");
        String email = DBString.scan.next();
        System.out.print("전공>>");
        String major = DBString.scan.next();

        pstmt = conn.prepareStatement(MemberSQL.updateMemberSQL);
        pstmt.setString(1, email);
        pstmt.setString(2, major);
        pstmt.setString(3, name);
        pstmt.executeUpdate();

        System.out.printf("%s %s %s 업데이트됨\n", name, email, major);
    }

    public void memberUpdate() throws ClassNotFoundException, SQLException  {
        System.out.println("\n==== 업데이트할 회원 정보 ====");
        System.out.print("조회이름>> ");
        String name = DBString.scan.next();

        DBConnection();

        pstmt = conn.prepareStatement(MemberSQL.selectNameSQL);
        pstmt.setString(1, name);
        rs = pstmt.executeQuery();

        selectDisplay(rs);

        updateMember(name);
    }
}

public class memberDB {
    public static void menuDisplay() {
        System.out.println("-------------------------------------------");
        System.out.println("1.회원등록 2.회원목록 3.회원수정 4.회원삭제 5.회원조회 6.종료");
        System.out.println("-------------------------------------------");
        System.out.print(">> ");
    }
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        MemberDBMgmt dbMgmt = new MemberDBMgmt();
        boolean flag = true;
        while(flag) {
            menuDisplay();
            int key = DBString.scan.nextInt();
            switch (key) {
                case 1:
                    dbMgmt.memberAdd();
                    break;
                case 2:
                    dbMgmt.memberSelectAll();
                    break;
                case 3:
                    dbMgmt.memberUpdate();
                    break;
                case 4:
                    dbMgmt.memberDeleteWithEmail();
                    break;
                case 5:
                    dbMgmt.memberSelectWithEmail();
                    break;
                case 6:
                    System.out.println("=========== 프로그램 종료. ===========");
                    flag = false;
                    break;
                default:
                    break;
            }

        }
    }
}
