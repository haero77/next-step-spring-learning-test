package cholog;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.Objects;

@Repository
public class UpdatingDAO {

    private JdbcTemplate jdbcTemplate;

    public UpdatingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    private final RowMapper<Customer> actorRowMapper = (resultSet, rowNum) -> {
        Customer customer = new Customer(
                resultSet.getLong("id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name")
        );
        return customer;
    };
    추후 rowMapper에 대해 학습해보고 이용해보기
    */

    /**
     * public int update(String sql, @Nullable Object... args)
     */
    public void insert(Customer customer) {
        //todo: customer를 디비에 저장하기
        final String sql = """
                insert into customers (
                    first_name,
                    last_name
                ) values (?, ?)""";

        jdbcTemplate.update(sql, customer.getFirstName(), customer.getLastName());
    }

    /**
     * public int update(String sql, @Nullable Object... args)
     */
    public int delete(Long id) {
        //todo: id에 해당하는 customer를 지우고, 해당 쿼리에 영향받는 row 수반환하기
        final String countSql = """
                select count(*)
                from customers
                where id = ?""";

        final Integer affectedRowCount = jdbcTemplate.queryForObject(countSql, Integer.class, id);

        final String deleteSql = """
                delete from customers c
                where c.id = ?""";
        jdbcTemplate.update(deleteSql, id);

        return affectedRowCount;
    }

    /**
     * public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
     */
    public Long insertWithKeyHolder(Customer customer) {
        String sql = "insert into customers (first_name, last_name) values (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //todo : keyHolder에 대해 학습하고, Customer를 저장후 저장된 Customer의 id를 반환하기
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            return ps;
        }, keyHolder);

        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }
}
