package ru.netology.servlet;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.exception.NotFoundException;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
  public static final String METHOD_GET="GET";
  public static final String METHOD_POST="POST";
  public static final String METHOD_DELETE="DELETE";
  public static final String PATH="/api/posts";
  private PostController controller;

  @Override
  public void init() {
      final var context = new AnnotationConfigApplicationContext("ru.netology");

      // получаем по имени бина
      final var controller = context.getBean("postController");

      // получаем по классу бина
      final var service = context.getBean(PostService.class);

      // по умолчанию создаётся лишь один объект на BeanDefinition
      final var isSame = service == context.getBean("postService");
  }


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // если деплоились в root context, то достаточно этого
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(METHOD_GET) && path.equals(PATH)) {
        controller.all(resp);
        return;
      }
      if (method.equals(METHOD_GET) && path.matches(PATH+"/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(METHOD_POST) && path.equals(PATH)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(METHOD_DELETE) && path.matches(PATH+"/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(path.lastIndexOf("/")));
        controller.removeById(id, resp);
        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }

