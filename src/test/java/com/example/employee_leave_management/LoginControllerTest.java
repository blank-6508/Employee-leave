package com.example.employee_leave_management;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.employee_leave_management.controller.LoginController;
import com.example.employee_leave_management.domain.Employee;
import com.example.employee_leave_management.repository.EmployeeRepository;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeRepository repo;

    @Test
    public void testManagerLogin() throws Exception {
        Employee manager = new Employee();
        manager.setEmployeename("testmanager");
        manager.setPassword("testpassword");
        manager.setRole("MANAGER");

        when(repo.findByEmployeenameAndPassword("testmanager", "testpassword"))
                .thenReturn(manager);

        mockMvc.perform(post("/login")
                .param("employeename", "testmanager")
                .param("password", "testpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managerdashboard.html"));
    }

    @Test
    public void testEmployeeLogin() throws Exception {
        Employee employee = new Employee();
        employee.setEmployeename("testuser");
        employee.setPassword("testpassword");
        employee.setRole("EMPLOYEE");

        when(repo.findByEmployeenameAndPassword("testuser", "testpassword"))
                .thenReturn(employee);

        mockMvc.perform(post("/login")
                .param("employeename", "testuser")
                .param("password", "testpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employeedashboard.html?employeeId=null&employeeName=testuser"));
    }

    @Test
    public void testFailedLogin() throws Exception {
        when(repo.findByEmployeenameAndPassword("wronguser", "wrongpassword"))
                .thenReturn(null);

        mockMvc.perform(post("/login")
                .param("employeename", "wronguser")
                .param("password", "wrongpassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login.html?error=true"));
    }
}