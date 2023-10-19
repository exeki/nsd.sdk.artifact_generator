package tests

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.ekazantsev.nsd_fake_class_generator.data.DbAccess
import ru.ekazantsev.nsd_fake_class_generator.nsd_connector.FakeApiConnector
import ru.ekazantsev.nsd_basic_api_connector.ConnectorParams

class TestUtils {
    static Logger logger = LoggerFactory.getLogger(getClass())
    static ConnectorParams connectorParams = ConnectorParams.byConfigFile("DSO_TEST")
    static FakeApiConnector nsdFakeApi = new FakeApiConnector(connectorParams, true)
    static ObjectMapper objectMapper = new ObjectMapper()
    static DbAccess db = DbAccess.getInstance()
}
