package com.example.sns.repository;

import com.example.sns.entity.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

//참고 JdbcTemplate는 sql을 매핑해서 쿼리를 날리는 것일 뿐 Table을 자동으로 구성해주지 않음. 테이블 생성, 라이브러리 등등 이걸 전부 자동으로 수행하는 것은 JPA(ORM)

@Repository //데이터베이스(DB)랑 상호작용(Repository)한다는 어노테이션, SQL Mapper을 이용해서 직접적인 DB의 sql 조작을 수행한다(sql쿼리), sql직접 안날리고 함수 형태로 조작한다
@RequiredArgsConstructor //[Lombok 기능] final이 붙은 변수가 있으면, 그 변수를 채워주는 생성자를 자동으로 만들어줌
public class JDBCRepository {

    private final JdbcTemplate jdbcTemplate; // 그냥 new JdbcTemplate()을 해버리면 껍데기만 있는 객체만 만들어짐(생성자없음)
    //final로 스프링이 켜질때 자동으로 만들어지는 DB에 접속하기 위한 ID,PW등등이 담긴 자동생성된 JdbcTemplate에 실제 JdbcTemplate 변수들을 DI(의존성 주입)한다

    public Users findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, userRowMapper(), username); //queryForObject를 사용해 단건 조회 및 매핑
        } catch (EmptyResultDataAccessException e) {
            return null;
            //DB에 없을 경우 null
        }
    }

    //DB에서 가져온 표 형태의 데이터(ResultSet)를 Users 객체로 조립해주는 메서드
    private RowMapper<Users> userRowMapper() {
        return (rs, rowNum) -> {
            Users user = new Users();
            // rs.get데이터타입("DB컬럼명") 형식으로 데이터를 꺼내어 Users 객체에 넣습니다.
            user.setId(rs.getInt("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user; //마지막에 자기 자신 반환
        };
    }
}
