/**
 * author:
 * version:
 * since:
 * description:
 */

package tests.gen

import ru.kazantsev.nsd.sdk.client.MetainfoUpdateService
import ru.kazantsev.nsd.sdk.artifact_generator.JarGeneratorService

import static tests.TestUtils.getConnectorParams
import static tests.TestUtils.getDb
import static tests.TestUtils.artifactConstants

new MetainfoUpdateService(connectorParams, db).fetchMeta()
JarGeneratorService get = new JarGeneratorService(artifactConstants, db)
get.generate()