# Contribuições, Sprint 2 (Lab01S02), Filipe Melo

## Semana 1

Contribuição: fiquei responsável pela agregação da estante, mantendo a mesma divisão da Sprint 1
(casos de uso 01 a 04). Modelei em PlantUML as classes Usuario, Aluno, Estante, ItemEstante,
Licenca e o enum TipoLeitura, e criei as classes Java correspondentes com atributos, construtores e
stubs dos métodos.

Decisões: criei a classe ItemEstante entre Estante e EBook, em vez de guardar o tipo de leitura
dentro de EBook. O mesmo eBook pode ser obrigatório para um aluno e livre para outro, então o tipo
pertence ao vínculo, não ao livro.

Usei agregação (o--) entre ItemEstante e EBook, porque o eBook continua existindo no catálogo
depois de ser removido da estante, e composição (*--) entre Aluno e Estante e entre Estante e
ItemEstante, porque nenhuma das duas existe sem o seu dono.

Tirei o tipo de leitura da assinatura de adicionarEBook. O aluno não informa mais se a leitura é
obrigatória ou livre: o método classificarLeitura consulta as disciplinas em que ele está
matriculado e deriva o tipo, para que não seja possível declarar como obrigatória uma leitura que a
disciplina não indicou.

Uso de IA: usei o Claude (Anthropic) para revisar o alinhamento entre o diagrama de classes e o
código Java e para conferir se toda classe podia ser rastreada até um caso de uso.
