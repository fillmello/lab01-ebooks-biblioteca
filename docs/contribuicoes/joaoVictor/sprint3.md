# Contribuições, Sprint 3 (Lab01S03), João Victor

## Semana 1

Contribuição: mantivemos a divisão das sprints anteriores e fiquei com os casos 05 a 08.
Implementei o cadastro de eBook (`Bibliotecario.cadastrarEBook`), a consulta dos alunos que têm um
título na estante (`Bibliotecario.consultarAlunosComEBook`), o registro das estatísticas de uso
(`SistemaEstatisticas`) e a renovação do catálogo (`Catalogo.renovar` e `PeriodoAcesso`).

Decisões: o cadastro lança `IllegalArgumentException` quando falta um dado obrigatório ou quando o
título já existe no catálogo, porque é um erro de preenchimento que a interface precisa explicar ao
bibliotecário; o menu captura a exceção e mostra a mensagem. `Catalogo.renovar` devolve um catálogo
novo, do semestre seguinte, em vez de alterar o atual, para deixar claro na apresentação quais
títulos renovaram a licença e quais saíram.

Uso de IA: usei o Claude (Anthropic) para revisar as mensagens de erro do cadastro e conferir se a
regra dos 3 alunos estava aplicada como o enunciado descreve.

## Semana 2

Contribuição: implementei, junto com o Filipe, a gravação e a leitura dos dados em arquivo
(`BibliotecaRepositorioArquivo`), ficando com a parte do catálogo, dos eBooks e das estatísticas, e
montei o menu do bibliotecário, incluindo o cadastro dos períodos de acesso do semestre.

Decisões: usei ponto e vírgula como separador dos campos, como sugere o roteiro, por ser simples de
conferir e depurar. O semestre e os períodos de acesso ficaram em um arquivo separado dos eBooks
(`catalogo.txt`), porque mudam em ritmos diferentes: os períodos são poucos e definidos pela
biblioteca, enquanto o catálogo cresce a cada cadastro. Ao renovar, os títulos que perderam a
licença saem também das estantes dos alunos, para não sobrar na estante um eBook que não está mais
licenciado.

Uso de IA: usei o Claude (Anthropic) para revisar a gravação e a leitura dos arquivos e conferir se
o catálogo renovado continuava consistente com as estantes.
