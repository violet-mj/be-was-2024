package db;

import exception.SQLRuntimeException;
import model.Post;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * H2 데이터 베이스를 이용한 db 로직
 * USER와 POST 처리 둘다 한 클래스안에 존재
 * @Author minjun kim
 */
public class H2Database {
  private static final Logger logger = LoggerFactory.getLogger(H2Database.class);


  private static class LazyHolder {
    private static final H2Database INSTANCE = new H2Database();
  }

  public static H2Database getInstance() {
    return LazyHolder.INSTANCE;
  }

  private H2Database() {}

  public void addUser(User user) {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = DatabaseConfig.getConnection();
      String sql = "INSERT INTO USERS (name, user_id, email, password) VALUES (?, ?, ?, ?)";
      statement = connection.prepareStatement(sql);
      statement.setString(1, user.getName());
      statement.setString(2, user.getUserId());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getPassword());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if(connection != null) {
          connection.close();
        }

        if(statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        throw new RuntimeException("db 오류", e);
      }
    }
  }

  public User findUserById(String userId) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    User user = null;
    logger.debug("userId = {}", userId);
    try {
      connection = DatabaseConfig.getConnection();
      String sql = "SELECT * FROM USERS WHERE user_id = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, userId);
      resultSet = statement.executeQuery();

      logger.debug("size = {}", resultSet.getFetchSize());

      while(resultSet.next()) {
        String name = resultSet.getString("name");
        String userId1 = resultSet.getString("user_id");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        logger.debug("name = {} userId = {} password = {} email = {}", name, userId1, password, email);
        user = new User(userId1, password, name, email);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if(connection != null) {
          connection.close();
        }

        if(statement != null) {
          statement.close();
        }

        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e) {
        throw new SQLRuntimeException("db 오류", e);
      }
    }

    return user;
  }

  public Collection<User> findAll() {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    List<User> users = new ArrayList<>();
    try {
      connection = DatabaseConfig.getConnection();
      String sql = "SELECT * FROM USERS";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      logger.debug("size = {}", resultSet.getFetchSize());

      while (resultSet.next()) {
        String name = resultSet.getString("name");
        String userId1 = resultSet.getString("user_id");
        String password = resultSet.getString("password");
        String email = resultSet.getString("email");
        logger.debug("name = {} userId = {} password = {} email = {}", name, userId1, password, email);
        users.add(new User(userId1, password, name, email));
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException("sql 에러", e);
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }

        if (statement != null) {
          statement.close();
        }

        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e) {
        throw new SQLRuntimeException("sql 에러", e);
      }
    }
    return users;
  }

  public void addPost(Post post) {
    Connection connection = null;
    PreparedStatement statement = null;
    try {
      connection = DatabaseConfig.getConnection();
      String sql = "INSERT INTO POSTS (title, content, image) VALUES (?, ?, ?)";
      statement = connection.prepareStatement(sql);
      statement.setString(1, post.getTitle());
      statement.setString(2, post.getContent());
      statement.setString(3, post.getImage());
      statement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if(connection != null) {
          connection.close();
        }
        if(statement != null) {
          statement.close();
        }
      } catch (SQLException e) {
        throw new RuntimeException("db 오류", e);
      }
    }
  }

  public Collection<Post> findAllPost () {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    List<Post> posts = new ArrayList<>();
    try {
      connection = DatabaseConfig.getConnection();
      String sql = "SELECT * FROM POSTS";
      statement = connection.prepareStatement(sql);
      resultSet = statement.executeQuery();
      logger.debug("size = {}", resultSet.getFetchSize());

      while (resultSet.next()) {
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        String image = resultSet.getString("image");
        posts.add(new Post(title, content, image));
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }

        if (statement != null) {
          statement.close();
        }

        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e) {
        throw new RuntimeException(e.getMessage());
      }
    }
    return posts;
  }

  public Post findPostById(String postId) {
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    Post post = null;
    try {
      connection = DatabaseConfig.getConnection();
      String sql = "SELECT * FROM POSTS WHERE id = ?";
      statement = connection.prepareStatement(sql);
      statement.setString(1, postId);
      resultSet = statement.executeQuery();

      logger.debug("size = {}", resultSet.getFetchSize());

      while(resultSet.next()) {
        String title = resultSet.getString("title");
        String content = resultSet.getString("content");
        String image = resultSet.getString("image");
        post = new Post(title, content, image);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } finally {
      try {
        if(connection != null) {
          connection.close();
        }

        if(statement != null) {
          statement.close();
        }

        if (resultSet != null) {
          resultSet.close();
        }
      } catch (SQLException e) {
        throw new SQLRuntimeException("db 오류", e);
      }
    }

    return post;
  }
}
