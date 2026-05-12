package com.example.sns.repository;

import com.example.sns.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

//참고 JdbcTemplate는 sql을 매핑해서 쿼리를 날리는 것일 뿐 Table을 자동으로 구성해주지 않음. 테이블 생성, 라이브러리 등등 이걸 전부 자동으로 수행하는 것은 JPA(ORM)
//JPA의 경우, JpaRepository를 상속받아 해당 인터페이스에 함수 선언을 하는 것으로 DB가 전부 구성됨
@Repository //데이터베이스(DB)랑 상호작용(Repository)한다는 어노테이션, SQL Mapper을 이용해서 직접적인 DB의 sql 조작을 수행한다(sql쿼리), sql직접 안날리고 함수 형태로 조작한다
@RequiredArgsConstructor //[Lombok 기능] final이 붙은 변수가 있으면, 그 변수를 채워주는 생성자를 자동으로 만들어줌
public class JDBCRepository {

    private final JdbcTemplate jdbcTemplate; // 그냥 new JdbcTemplate()을 해버리면 껍데기만 있는 객체만 만들어짐(생성자없음)
    //final로 스프링이 켜질때 자동으로 만들어지는 DB에 접속하기 위한 ID,PW등등이 담긴 자동생성된 JdbcTemplate에 실제 JdbcTemplate 변수들을 DI(의존성 주입)한다

    //01 유저 저장
    //행은 튜플, 열은 어트리뷰트 이건 기억하자
    public int save(Users user) {
        String sql = "INSERT INTO users (username, password, email, role, created_at) VALUES (?, ?, ?, ?, ?)";
        // update() 메서드는 INSERT, UPDATE, DELETE 에 모두 사용되고 영향받은 행(튜플)의 개수 반환
        return jdbcTemplate.update(sql,
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    //02 단건 조회(검색은 아이디로 하자)
    public Users findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null; // 데이터가 없으면 null 반환
        }
    }

    //03 다건 조회(Read로 전체 목록 조회에 해당함)
    public List<Users> findAll() {
        String sql = "SELECT * FROM users";
        // query()는 결과가 여러 개(List)일 때 사용 //이거 기억
        return jdbcTemplate.query(sql, userRowMapper());
    }

    //04 정보 수정(Update에 해당함)
    public int updatePassword(Long id, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newPassword, id);
    }

    //05 삭제
    public int deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    //DB에서 가져온 표 형태의 데이터(ResultSet)를 Users 객체로 조립해주는 메서드
    private RowMapper<Users> userRowMapper() {
        return (rs, rowNum) -> {
            Users user = new Users();
            // rs.get데이터타입("DB컬럼명") 형식으로 데이터를 꺼내어 Users 객체에 넣습니다.
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setEmail(rs.getString("email"));
            user.setRole(rs.getString("role"));

            //Timestamp는 자바의 LocalDateTime으로 변환 //이거 꼭 해줘야 함
            if (rs.getTimestamp("created_at") != null) {
                user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }
            return user; //마지막에 자기 자신 반환
        };
    }
}
