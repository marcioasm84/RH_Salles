Este respositório tem duas Branches: main e usandorabbitmq

main:
Esta versão, é a mais básica, que contempla a implementação do que foi solicitado no teste.

usandorabbitmq:

Nesta versão, foi adicionado o RabbitMQ.
A funcionalidade de envio de arquivo CSV, é feita de forma assincrona, onde o usuário envia o arquivo e recebe imediatamente a respota que a importaçaõ será realizada.
Isso é feito, colocando o arquivo em uma pasta upload e será colocado na fila do RabbitMQ para ser importado através de um Produtor. Então um Consumidor, recebe essa messagem e 
chama o service que irá ler o arquivo e importará na base de dados e mudará o arquivo para a pasta processado.
Para isso funcionar é necessário criar as duas pastas no diretorio: C:/Projetos/Projeto_RH_Salles/upload  e  C:/Projetos/Projeto_RH_Salles/upload/processado
ou então modificar o application.yaml com um novo caminho.

Possa ser que nessa versão, os teste unitários venham a falhar, já que foi modificado o comportamento da funcionalidade.
