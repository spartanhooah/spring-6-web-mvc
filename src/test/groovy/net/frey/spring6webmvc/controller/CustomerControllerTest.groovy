package net.frey.spring6webmvc.controller

import net.frey.spring6webmvc.service.CustomerService
import net.frey.spring6webmvc.service.CustomerServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.hamcrest.core.Is.is

@WebMvcTest(CustomerController)
class CustomerControllerTest extends Specification {
    @Autowired
    MockMvc mockMvc

    @SpringBean
    CustomerService customerService = Mock()

    def impl = new CustomerServiceImpl()

    def "get customer by ID"() {
        given:
        def testCustomer = impl.listCustomers()[0]
        customerService.getCustomerById(testCustomer.id) >> testCustomer

        expect:
        mockMvc.perform(get("/api/v1/customer/$testCustomer.id")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath('$.id', is(testCustomer.id.toString())))
            .andExpect(jsonPath('$.name', is(testCustomer.name)))
    }

    def "get all customers"() {
        given:
        customerService.listCustomers() >> impl.listCustomers()

        expect:
        mockMvc.perform(get("/api/v1/customer")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath('$.length()', is(2)))
    }
}
