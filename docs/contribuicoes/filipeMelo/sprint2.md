# Contribuições, Sprint 2 (Lab01S02), Filipe Melo

## Semana 1

Contribuição: mantivemos a divisão da Sprint 1 e fiquei com a agregação da estante (casos 01 a 04).
Modelei em PlantUML as classes Usuario, Aluno, Estante, ItemEstante, Licenca e o enum TipoLeitura,
e criei as classes Java correspondentes com atributos, construtores e stubs dos métodos.

Decisões: criei a classe ItemEstante entre Estante e EBook, em vez de guardar o tipo de leitura
dentro de EBook, porque o mesmo eBook pode ser obrigatório para um aluno e livre para outro. Usei
agregação entre ItemEstante e EBook, já que o eBook continua no catálogo depois de sair da
estante, e composição entre Aluno e Estante, que não existe sem o dono.

Uso de IA: usei o Claude (Anthropic) para revisar o alinhamento entre o diagrama de classes e o
código Java. Conferi os nomes dos métodos antes do commit.
