package tests

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.kazantsev.nsd.sdk_data.DbAccess

class TestUtils {
    static Logger logger = LoggerFactory.getLogger(getClass())
    static ObjectMapper objectMapper = new ObjectMapper()
    static DbAccess db = DbAccess.getInstance()
}
