package cu.drones.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import cu.drones.persistence.Drone;
import cu.drones.persistence.Medication;
import cu.drones.persistence.model.Model;
import cu.drones.persistence.model.State;
import cu.drones.persistence.repositories.DroneRepository;
import cu.drones.persistence.repositories.MedicationRepository;
import cu.drones.utils.AppUrls;
import cu.drones.utils.UtilsHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DispatchControllerTest {

    private JacksonTester<List<Drone>> listResponseDrone;

    private JacksonTester<Drone> droneResponse;

    private JacksonTester<List<Medication>> listResponseMed;

    private JacksonTester<Medication> medResponse;

    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mvc;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private DroneRepository droneRepository;

    public DispatchControllerTest() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @SneakyThrows
    @Test
    @Order(1)
    public void registerDrone_ShouldReturnNewDroneSuccessfully() {
//        given
        Drone drone = new Drone(UtilsHelper.DRONE_OK_SERIAL_NUMBER, Model.CRUISER_WEIGHT, 300, 95, State.IDLE);

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is2xxSuccessful()).andReturn().getResponse();

//        then
        Drone dronedb = droneResponse.parse(response.getContentAsString()).getObject();
        Assert.notNull(dronedb, "New drone should be returned");
        Assert.notNull(dronedb.getDroneid(), "New id drone should be set");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void loadDroneWithMedications_ShouldReturnDroneLoaded() {
//        given
        List<Medication> meds = new ArrayList<>();
        for (int i = 0; i < UtilsHelper.MEDICATION_CODES.length; i++)
            meds.add(new Medication(UtilsHelper.MEDICATION_NAMES[i], 100, UtilsHelper.MEDICATION_CODES[i], UtilsHelper.generateByteArray()));

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.LOAD_MEDS + UtilsHelper.DRONE_OK_SERIAL_NUMBER).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(meds))).andExpect(status().is2xxSuccessful()).andReturn().getResponse();

//        then
        Drone drone = droneResponse.parse(response.getContentAsString()).getObject();
        Assert.notNull(drone, "Updated to LOADED drone should be returned");
    }

    @SneakyThrows
    @Test
    @Order(3)
    public void getDroneMedications_ShouldReturnMedications() {
//        when
        MockHttpServletResponse response = mvc.perform(get(AppUrls.LOAD_MEDS + UtilsHelper.DRONE_OK_SERIAL_NUMBER)).andExpect(status().isOk()).andReturn().getResponse();

//        then
        List<Medication> medicationList = listResponseMed.parse(response.getContentAsString()).getObject();
        Assert.notEmpty(medicationList, "List of drone medications should be returned");
    }

    @SneakyThrows
    @Test
    @Order(4)
    public void getDronesForLoading_ShouldReturnAvailableDrones() {
//        given
        Drone drone = new Drone(UtilsHelper.DRONE_OK_SERIAL_NUMBER_2, Model.LIGHT_WEIGHT, 100, 100, State.IDLE);
        MockHttpServletResponse resp = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is2xxSuccessful()).andReturn().getResponse();

        Drone dronedb = droneResponse.parse(resp.getContentAsString()).getObject();
        Assert.notNull(dronedb, "An IDLE drone should be returned");

//        when
        MockHttpServletResponse response = mvc.perform(get(AppUrls.DRONES_FORLOADING).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse();

//        then
        List<Drone> droneList = listResponseDrone.parse(response.getContentAsString()).getObject();
        Assert.notNull(droneList, "List of drones should not be null");
        Assert.notEmpty(droneList, "List of drones should not be empty");
    }

    @SneakyThrows
    @Test
    @Order(5)
    public void checkDroneBattery_ShouldReturnDroneBatteryLevel() {
//        when
        MockHttpServletResponse response = mvc.perform(get(AppUrls.DRONE_BATTERY + UtilsHelper.DRONE_OK_SERIAL_NUMBER_2)).andExpect(status().isOk()).andReturn().getResponse();

//        then
        String level = response.getContentAsString();
        Assert.notNull(level, "Level should not be null");
        Assert.hasText("100", "Level should be 100 % 'cause drone is IDLE");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void getDrones_ShouldReturnDroneList() {
//        when
        MockHttpServletResponse response = mvc.perform(get(AppUrls.DRONES_DISPATCHER_URL)).andExpect(status().isOk()).andReturn().getResponse();

//        then
        List<Drone> droneList = listResponseDrone.parse(response.getContentAsString()).getObject();
        Assert.notNull(droneList, "Drone list should not be null");
    }

    //    Error Cases Test
    @SneakyThrows
    @Test
    @Order(2)
    public void registerDuplicatedDrone_ShouldReturnError() {
//        given
        Drone drone = new Drone(UtilsHelper.DRONE_OK_SERIAL_NUMBER, Model.CRUISER_WEIGHT, 300, 95, State.IDLE);

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is4xxClientError()).andReturn().getResponse();

//        then
        Assert.isTrue(response.getContentAsString().contains("A drone already exist with that serialNumber"), "Should have message 'A drone already exist with that serialNumber'");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void registerDroneWithSerialMissing_ShouldReturnMissingField() {
        //        given
        Drone drone = new Drone(null, Model.CRUISER_WEIGHT, 300, 95, State.IDLE);

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is4xxClientError()).andReturn().getResponse();

//        then
        Assert.isTrue(response.getContentAsString().contains("At field serialNumber must not be blank"), "Should have message 'At field serialNumber must not be blank'");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void registerDroneWithBadModelAndWeight_ShouldReturnModelAndDoesntMatch() {
//        given
        Drone drone = new Drone("Drone X", Model.CRUISER_WEIGHT, 100, 95, State.IDLE);

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is4xxClientError()).andReturn().getResponse();

//        then
        Assert.isTrue(response.getContentAsString().contains("Model and weight value doesn't match"), "Should have message 'Model and weight value doesn't match'");
    }

    @SneakyThrows
    @Test
    @Order(6)
    public void loadDroneWithMissingMedications_ShouldReturnErrorNotFound() {
        mvc.perform(post(AppUrls.LOAD_MEDS + UtilsHelper.DRONE_OK_SERIAL_NUMBER_2).contentType(MediaType.APPLICATION_JSON)).andExpect(status().is4xxClientError()).andReturn();
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void loadDroneWithMedicationsWithLevelLowerThan25Percent_ShouldReturnErrorMessage() {
//        given
        String serialNumber = "Drone XYZ";
        Drone drone = new Drone(serialNumber, Model.CRUISER_WEIGHT, 300, 25, State.IDLE);

        MockHttpServletResponse resp = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is2xxSuccessful()).andReturn().getResponse();
        Drone dronedb = droneResponse.parse(resp.getContentAsString()).getObject();
        Assert.notNull(dronedb, "Should return new drone created");

        List<Medication> meds = new ArrayList<>();
        for (int i = 0; i < UtilsHelper.MEDICATION_CODES.length; i++)
            meds.add(new Medication(UtilsHelper.MEDICATION_NAMES[i], 100, UtilsHelper.MEDICATION_CODES[i], UtilsHelper.generateByteArray()));

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.LOAD_MEDS + serialNumber).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(meds))).andExpect(status().is4xxClientError()).andReturn().getResponse();

//        then
        Assert.isTrue(response.getContentAsString().contains("Can't load drone with medications 'cause battery capacity is lower than 25 percent"), "Should have message 'Can't load drone with medications 'cause battery capacity is lower than 25 percent'");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void loadDroneWithMedicationsWithWrongStatus_ShouldReturnErrorMessage() {
//        given
        String serialNumber = "Drone X";
        Drone drone = new Drone(serialNumber, Model.CRUISER_WEIGHT, 300, 100, State.LOADING);

        MockHttpServletResponse resp = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is2xxSuccessful()).andReturn().getResponse();
        Drone dronedb = droneResponse.parse(resp.getContentAsString()).getObject();
        Assert.notNull(dronedb, "Should return new drone created");

        List<Medication> meds = new ArrayList<>();
        for (int i = 0; i < UtilsHelper.MEDICATION_CODES.length; i++)
            meds.add(new Medication(UtilsHelper.MEDICATION_NAMES[i], 100, UtilsHelper.MEDICATION_CODES[i], UtilsHelper.generateByteArray()));

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.LOAD_MEDS + serialNumber).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(meds))).andExpect(status().is4xxClientError()).andReturn().getResponse();

//        then
        Assert.isTrue(response.getContentAsString().contains("Can't load drone with medications 'cause it's status"), "Should have message 'Can't load drone with medications 'cause it's status'");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void loadDroneWithMedicationsWithOverWeight_ShouldReturnErrorMessage() {
//        given
        String serialNumber = "Drone XZY";
        Drone drone = new Drone(serialNumber, Model.CRUISER_WEIGHT, 300, 100, State.IDLE);

        MockHttpServletResponse resp = mvc.perform(post(AppUrls.DRONES_DISPATCHER_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(drone))).andExpect(status().is2xxSuccessful()).andReturn().getResponse();
        Drone dronedb = droneResponse.parse(resp.getContentAsString()).getObject();
        Assert.notNull(dronedb, "Should return new drone created");

        List<Medication> meds = new ArrayList<>();
        for (int i = 0; i < UtilsHelper.MEDICATION_CODES.length; i++)
            meds.add(new Medication(UtilsHelper.MEDICATION_NAMES[i], 150, UtilsHelper.MEDICATION_CODES[i], UtilsHelper.generateByteArray()));

//        when
        MockHttpServletResponse response = mvc.perform(post(AppUrls.LOAD_MEDS + serialNumber).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(meds))).andExpect(status().is4xxClientError()).andReturn().getResponse();

//        then
        Assert.isTrue(response.getContentAsString().contains("Specified medications exceeds drone's capacity"), "Should have message 'Specified medications exceeds drone's capacity'");
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void getNonExistingDroneMedications_ShouldReturnNotFound() {
//        when
        mvc.perform(get(AppUrls.LOAD_MEDS + "Drone Z")).andExpect(status().isNotFound()).andReturn().getResponse();
    }

    @SneakyThrows
    @Test
    @Order(2)
    public void checkNonExistingDroneBattery_ShouldReturnNotFound() {
//        when
        mvc.perform(get(AppUrls.DRONE_BATTERY + "Drone Z")).andExpect(status().isNotFound()).andReturn().getResponse();
    }
}