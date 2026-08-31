# Contribuições, Sprint 3 (Lab01S03), João Victor

## Semana 1

Contribuição: mantivemos a divisão das sprints anteriores e cada um implementou as classes que já
tinha modelado. Fiquei com os casos 05 a 08 e com as classes que desenhei na Sprint 2:
`Bibliotecario`, `EBook`, `Catalogo`, `PeriodoAcesso` e `SistemaEstatisticas`. Substituí os stubs
pelo código de verdade, ou seja, o cadastro de eBook com os dados obrigatórios, a consulta dos
alunos que têm um título na estante, a contagem de adições por título e a renovação do catálogo,
que mantém apenas os eBooks presentes na estante de pelo menos 3 alunos. Na interface, montei o
menu do bibliotecário, incluindo o cadastro dos períodos de acesso do semestre e o cadastro de
usuários. Na persistência,
que fizemos juntos em `BibliotecaRepositorioArquivo`, fiquei com a parte do catálogo, dos eBooks e
das estatísticas.

Decisões: o cadastro lança `IllegalArgumentException` quando falta um dado obrigatório ou quando o
título já existe no catálogo, porque é um erro de preenchimento que a interface precisa explicar ao
bibliotecário; o menu captura a exceção e mostra a mensagem. `Catalogo.renovar` devolve um catálogo
novo, do semestre seguinte, em vez de alterar o atual, para deixar claro na apresentação quais
títulos renovaram a licença e quais saíram. Usei ponto e vírgula como separador dos campos, como
sugere o roteiro, por ser simples de conferir e depurar, e o semestre e os períodos de acesso
ficaram em um arquivo separado dos eBooks (`catalogo.txt`), porque mudam em ritmos diferentes: os
períodos são poucos e definidos pela biblioteca, enquanto o catálogo cresce a cada cadastro. Ao
renovar, os títulos que perderam a licença saem também das estantes dos alunos, para não sobrar na
estante um eBook que não está mais licenciado. O cadastro de usuários ficou no menu do
bibliotecário porque o enunciado diz que é a equipe da biblioteca que mantém as informações sobre os
bibliotecários e os alunos; é por lá que se cria um bibliotecário novo, já que a tela inicial, aberta
a qualquer um, só cria aluno.

Uso de IA: usei o Claude (Anthropic) para revisar as mensagens de erro do cadastro, conferir se a
regra dos 3 alunos estava aplicada como o enunciado descreve e revisar a gravação e a leitura dos
arquivos. Conferi o resultado da renovação manualmente antes do commit.
