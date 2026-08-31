# Contribuições, Sprint 3 (Lab01S03), Filipe Melo

## Semana 1

Contribuição: mantivemos a divisão das sprints anteriores e fiquei com os casos 01 a 04. Implementei
o login (`Usuario.autenticar` e a tela de login do `MenuPrincipal`), a adição e a remoção de eBooks
na estante (`Estante.adicionar`, `Estante.remover` e os métodos correspondentes de `Aluno`) e o
acesso ao eBook com o limite de acessos simultâneos da `Licenca`.

Decisões: `autenticar` ficou em `Usuario`, e não nas subclasses, porque a regra de validação de
senha é a mesma para aluno e bibliotecário. A notificação do sistema de estatísticas ficou dentro
de `Aluno.adicionarEBook`, logo depois da adição dar certo, para garantir o `<<include>>` de UC02
para UC07: assim nenhuma chamada da interface consegue adicionar um eBook sem registrar a
estatística. Os métodos de estante retornam `boolean` em vez de lançar exceção, porque limite
atingido e período fechado são respostas esperadas do fluxo, não erros de programação; a interface
transforma o `false` em uma mensagem explicando o motivo.

Uso de IA: usei o Claude (Anthropic) para revisar o tratamento de erros do menu e conferir se as
regras dos casos 01 a 04 estavam de acordo com o enunciado. Testei os fluxos manualmente antes do
commit.

## Semana 2

Contribuição: implementei, junto com o João Victor, a gravação e a leitura dos dados em arquivo
(`BibliotecaRepositorioArquivo`), ficando com a parte das estantes e dos usuários, e integrei o
menu do aluno à persistência, gravando os dados a cada ação que altera a estante.

Decisões: o arquivo de estantes guarda a matrícula do aluno e o título do eBook, e não os objetos
inteiros, para não repetir em dois arquivos os dados que já estão em `usuarios.txt` e `ebooks.txt`.
Começamos com uma classe de repositório por arquivo, mas juntamos tudo em uma só ao ver que as
cinco eram o mesmo código repetido, mudando apenas os campos de cada linha.
Por causa disso, o título passou a identificar o eBook também no código, com `equals` e `hashCode`
em `EBook`. Criei o método `Estante.restaurar` para recolocar na estante os itens já gravados, sem
passar pela validação do período de acesso, que só vale para uma adição nova feita pelo aluno.

Uso de IA: usei o Claude (Anthropic) para revisar a leitura dos arquivos, principalmente o
tratamento de linhas incompletas e da primeira execução, quando os arquivos ainda não existem.

## Semana 3

Contribuição: acrescentei à tela de login a criação de conta para quem ainda não tem cadastro, com
as validações de id e matrícula já em uso e o login automático depois de criar.

Decisões: a tela só cria conta de aluno. O enunciado diz que é a equipe da biblioteca que mantém as
informações sobre os bibliotecários e os alunos, então deixar qualquer um se cadastrar como
bibliotecário daria acesso ao cadastro de eBooks e à renovação do catálogo. Aproveitei para barrar
o ponto e vírgula nos campos digitados, aqui e no cadastro de eBook, porque é o separador dos
arquivos de dados e quebraria a leitura na execução seguinte.

Uso de IA: usei o Claude (Anthropic) para revisar as validações e testar os casos de recusa.
