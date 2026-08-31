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

Contribuição: implementei a persistência das estantes e dos usuários
(`EstanteRepositorioArquivo` e `UsuarioRepositorioArquivo`) e integrei o menu do aluno à
persistência, gravando os dados a cada ação que altera a estante.

Decisões: o arquivo de estantes guarda a matrícula do aluno e o título do eBook, e não os objetos
inteiros, para não repetir em dois arquivos os dados que já estão em `usuarios.txt` e `ebooks.txt`.
Por causa disso, o título passou a identificar o eBook também no código, com `equals` e `hashCode`
em `EBook`. Criei o método `Estante.restaurar` para recolocar na estante os itens já gravados, sem
passar pela validação do período de acesso, que só vale para uma adição nova feita pelo aluno.

Uso de IA: usei o Claude (Anthropic) para revisar a leitura dos arquivos, principalmente o
tratamento de linhas incompletas e da primeira execução, quando os arquivos ainda não existem.
