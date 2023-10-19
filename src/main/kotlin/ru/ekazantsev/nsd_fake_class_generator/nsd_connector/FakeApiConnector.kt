package ru.ekazantsev.nsd_fake_class_generator.nsd_connector

import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.util.EntityUtils
import ru.ekazantsev.nsd_fake_class_generator.nsd_connector.dto.MetaClassWrapperDto
import ru.ekazantsev.nsd_basic_api_connector.Connector
import ru.ekazantsev.nsd_basic_api_connector.ConnectorParams

/**
 * Коннектор к NSD
 */
class FakeApiConnector(params: ConnectorParams) : Connector(params) {

    private val moduleBase: String = "modules.sdkController"
    private val paramsConst: String = "request,response,user"

    /**
     * Получить информацию о метаклассе
     * @param metaClassCode код метакласса, инфа по которому нужна
     * @return dto с информацией
     */
    fun getMetaClassInfo(metaClassCode: String): MetaClassWrapperDto {
        val methodName = "getMetaClassInfo"
        val response: CloseableHttpResponse = this.execGet(
            moduleBase + methodName,
            paramsConst,
            mapOf("meta" to metaClassCode)
        )
        val body: String = EntityUtils.toString(response.entity)
        return this.objectMapper.readValue(body, MetaClassWrapperDto::class.java)
    }

}