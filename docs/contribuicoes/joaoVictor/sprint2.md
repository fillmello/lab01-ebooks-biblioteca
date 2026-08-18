# Contribuições, Sprint 2 (Lab01S02), João Victor

## Semana 1

Contribuição: fiquei responsável pela agregação do catálogo, mantendo a mesma divisão da Sprint 1
(casos de uso 05 a 08). Modelei em PlantUML as classes Bibliotecario, EBook, Catalogo,
PeriodoAcesso, SistemaEstatisticas e os enums Formato e Categoria, e criei as classes Java
correspondentes com atributos, construtores e stubs dos métodos.

Decisões: usei agregação (o--) entre Catalogo e EBook, porque o eBook não deixa de existir quando
sai do catálogo de um semestre, e composição (*--) entre Catalogo e PeriodoAcesso, porque o período
só faz sentido dentro do semestre a que pertence.

Transformei formato e categoria em enums em vez de String, porque o enunciado lista valores fixos
(PDF/EPUB e literatura/técnico/periódico) e o enum impede que se cadastre um valor inválido.

Criei a classe Disciplina e o caso de uso UC09, que faltavam para o sistema saber quais eBooks são
de leitura obrigatória. Sem isso, o tipo dependia da declaração do aluno.

Uso de IA: usei o Claude (Anthropic) para revisar os nomes dos métodos e verificar se as
multiplicidades do diagrama batiam com as regras do enunciado.
