# Histórias de Usuário

Sistema de Gestão de eBooks da Biblioteca Universitária — Sprint 1 (Lab01S01).

Formato adotado: *Como \<persona>, \<função>, eu quero \<ação>, para que \<benefício>.*
Critérios INVEST: Independente, Negociável, Valiosa, Estimável, Small, Testável.

Personas usadas: **Bruno** (aluno), **Carla** (bibliotecária) e **Ana** (usuária genérica do
sistema, usada apenas na HU01, que vale para os dois perfis).

## Distribuição por integrante

Nesta sprint, **Filipe Melo** ficou responsável pelo diagrama de casos de uso em PlantUML
([casos-de-uso.puml](diagramas/casos-de-uso.puml)) e **João Victor** pelas histórias de usuário
deste arquivo. Cada história corresponde ao caso de uso de mesmo número.

| ID | Caso de uso / História | Ator |
| --- | --- | --- |
| 01 | Realizar login | Usuário do sistema |
| 02 | Adicionar eBook à estante | Aluno |
| 03 | Remover eBook da estante | Aluno |
| 04 | Acessar eBook | Aluno |
| 05 | Cadastrar eBook | Bibliotecário |
| 06 | Consultar alunos com um eBook | Bibliotecário |
| 07 | Registrar estatística de uso | Bibliotecário |
| 08 | Renovar catálogo do semestre | Bibliotecário |

## HU01 — Realizar login

Como Ana, usuária do sistema, eu quero informar minha senha para validar meu login, para que
apenas usuários autorizados acessem o sistema.

## HU02 — Adicionar eBook à estante

Como Bruno, aluno, eu quero adicionar à minha estante pessoal até 4 eBooks de leitura obrigatória
e 2 de leitura livre, para que eu possa acessá-los durante o semestre.

## HU03 — Remover eBook da estante

Como Bruno, aluno, eu quero remover eBooks adicionados anteriormente à minha estante, para que eu
possa ajustar minha estante durante o período de acesso.

## HU04 — Acessar eBook

Como Bruno, aluno, eu quero acessar um eBook da minha estante, para que eu possa lê-lo enquanto
houver licença de uso disponível, respeitado o limite de 60 acessos simultâneos.

## HU05 — Cadastrar eBook

Como Carla, bibliotecária, eu quero cadastrar os eBooks disponíveis a cada semestre, com título,
editora, formato e categoria, para que os alunos possam adicioná-los à sua estante.

## HU06 — Consultar alunos com um eBook

Como Carla, bibliotecária, eu quero saber quais alunos têm determinado eBook em sua estante, para
que eu possa acompanhar o uso do acervo.

## HU07 — Registrar estatística de uso

Como Carla, bibliotecária, eu quero que o sistema de estatísticas de uso seja notificado sempre
que um aluno adiciona um eBook à estante, para que a biblioteca possa acompanhar quais títulos são
mais utilizados pelos alunos.

## HU08 — Renovar catálogo do semestre

Como Carla, bibliotecária, eu quero manter no catálogo licenciado apenas os eBooks adicionados à
estante de pelo menos 3 alunos ao final do período de acesso, para que os títulos pouco utilizados
tenham a licença não renovada e sejam removidos.
