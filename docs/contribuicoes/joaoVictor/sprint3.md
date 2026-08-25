# Contribuições, Sprint 2 (Lab01S02), João Victor

## Semana 1

Contribuição: mantivemos a divisão da Sprint 1 
imprementação das classes dos casos de uso 5 ao 8. 

Modelei em PlantUML as classes Bibliotecario, EBook, Catalogo, PeriodoAcesso,
SistemaEstatisticas e os enums Formato e Categoria, e criei as classes Java correspondentes com
atributos, construtores e stubs dos métodos.

Decisões: usei agregação entre Catalogo e EBook, porque o eBook não deixa de existir quando
sai do catálogo de um semestre, e composição entre Catalogo e PeriodoAcesso, porque o período
só faz sentido dentro do semestre a que pertence.

Uso de IA: usei o Claude (Anthropic) para revisar os nomes dos métodos e conferir se as
multiplicidades batiam com as regras do enunciado.
