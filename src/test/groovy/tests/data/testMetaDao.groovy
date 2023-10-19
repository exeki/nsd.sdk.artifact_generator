package tests.data

import static tests.TestUtils.*

logger.info(
        db.metaClassDao.queryForEq("fullCode", "serviceCall\$serviceCall").first()
                .fullCode.split('\\$').join("\\\$")
)