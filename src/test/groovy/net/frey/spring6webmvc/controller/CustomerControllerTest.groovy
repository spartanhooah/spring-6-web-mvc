package net.frey.spring6webmvc.controller

import com.fasterxml.jackson.databind.ObjectMapper
import net.frey.spring6webmvc.config.SecurityConfig
import net.frey.spring6webmvc.model.dto.CustomerDTO
import net.frey.spring6webmvc.service.CustomerService
import net.frey.spring6webmvc.service.CustomerServiceImpl
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

import static java.util.UUID.randomUUID
import static net.frey.spring6webmvc.controller.CustomerController.PATH
import static org.hamcrest.core.Is.is
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@Import(SecurityConfig)
@WebMvcTest(CustomerController)
class CustomerControllerTest extends Specification {
    static final def USERNAME = "user1"
    static final def PASSWORD = "password"

    @Autowired
    MockMvc mockMvc

    @Autowired
    ObjectMapper mapper

    @SpringBean
    CustomerService customerService = Mock()

    CustomerService impl

    void setup() {
        impl = new CustomerServiceImpl()
    }

    def "get customer by ID"() {
        given:
        def testCustomer = impl.listCustomers()[0]
        customerService.getCustomerById(testCustomer.id) >> Optional.of(testCustomer)

        expect:
        mockMvc.perform(get("$PATH/$testCustomer.id")
            .with(httpBasic(USERNAME, PASSWORD))
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
        mockMvc.perform(get(PATH)
            .with(httpBasic(USERNAME, PASSWORD))
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath('$.length()', is(2)))
    }

    def "create a customer"() {
        given:
        def customer = impl.listCustomers()[0]
        customer.id = null
        customer.version = 0
        customerService.addCustomer(_ as CustomerDTO) >> impl.listCustomers()[1]

        expect:
        mockMvc.perform(post(PATH)
            .accept(MediaType.APPLICATION_JSON)
            .with(httpBasic(USERNAME, PASSWORD))
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(customer)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
    }

    def "update a customer"() {
        given:
        def customer = impl.listCustomers()[0]

        1 * customerService.updateCustomer(_ as UUID, _ as CustomerDTO) >> Optional.of(customer)

        expect:
        mockMvc.perform(put("$PATH/$customer.id")
            .with(httpBasic(USERNAME, PASSWORD))
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(customer)))
            .andExpect(status().isNoContent())
    }

    def "delete a customer"() {
        given:
        def customer = impl.listCustomers()[0]

        1 * customerService.delete({ it == customer.id }) >> true

        expect:
        mockMvc.perform(delete("$PATH/$customer.id")
            .with(httpBasic(USERNAME, PASSWORD)))
            .andExpect(status().isNoContent())
    }

    def "get customer by ID throws not found"() {
        given:
        customerService.getCustomerById(_ as UUID) >> Optional.empty()

        expect:
        mockMvc.perform(get("$PATH/${randomUUID()}")
            .with(httpBasic(USERNAME, PASSWORD)))
            .andExpect(status().isNotFound())
    }
}
