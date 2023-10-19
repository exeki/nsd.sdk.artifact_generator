package tests.fetchAndWrite

import ru.ekazantsev.nsd_fake_class_generator.services.MetainfoUpdateService

import static tests.TestUtils.*

Set<String> metaCodes = ["serviceCall", "employee", "task", "ou", 'agreement', 'team', 'root', 'slmService']
MetainfoUpdateService writer = new MetainfoUpdateService(connectorParams, metaCodes)
writer.fetchMeta()
