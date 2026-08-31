# Contribuições, Sprint 3 (Lab01S03), Filipe Melo

## Semana 1

Contribuição: mantivemos a divisão das sprints anteriores e cada um implementou as classes que já
tinha modelado. Fiquei com os casos 01 a 04 e com as classes que desenhei na Sprint 2: `Usuario`,
`Aluno`, `Estante`, `ItemEstante` e `Licenca`. Substituí os stubs pelo código de verdade, ou seja,
o login em `Usuario.autenticar`, a adição e a remoção de eBooks na estante respeitando os limites de
4 obrigatórios e 2 livres e o período de acesso, e o acesso ao eBook com o limite de acessos
simultâneos da licença. Na interface, montei a tela de login, o menu do aluno e a criação de conta
para quem ainda não tem cadastro. Na persistência, que fizemos juntos em
`BibliotecaRepositorioArquivo`, fiquei com a parte das estantes e dos usuários.

Decisões: `autenticar` ficou em `Usuario`, e não nas subclasses, porque a regra de validação de
senha é a mesma para aluno e bibliotecário. A notificação do sistema de estatísticas ficou dentro
de `Aluno.adicionarEBook`, logo depois de a adição dar certo, para garantir o `<<include>>` de UC02
para UC07: assim nenhuma chamada da interface consegue adicionar um eBook sem registrar a
estatística. Os métodos de estante retornam `boolean` em vez de lançar exceção, porque limite
atingido e período fechado são respostas esperadas do fluxo, não erros de programação; a interface
transforma o `false` em uma mensagem explicando o motivo. O arquivo de estantes guarda a matrícula
do aluno e o título do eBook, e não os objetos inteiros, para não repetir dados que já estão em
`usuarios.txt` e `ebooks.txt`.

Uso de IA: usei o Claude (Anthropic) para revisar o tratamento de erros do menu, conferir se as
regras dos casos 01 a 04 estavam de acordo com o enunciado e apoiar a estrutura da interface de
linha de comando, que está detalhada na nota de transparência do README. Testei os fluxos
manualmente antes do commit.
