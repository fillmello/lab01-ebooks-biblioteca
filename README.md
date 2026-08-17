# Sistema de Gestão de eBooks da Biblioteca Universitária

LAB01, Projeto I — Laboratório de Desenvolvimento de Software.

Repositório: <https://github.com/fillmello/lab01-ebooks-biblioteca>

## Descrição do sistema

Uma universidade pretende oferecer aos alunos um acervo de livros digitais (eBooks). A equipe da
biblioteca cadastra os eBooks disponíveis a cada semestre e mantém as informações sobre os eBooks,
os bibliotecários e os alunos.

Cada eBook possui um título, uma editora, um formato de arquivo (por exemplo, PDF ou EPUB) e
pertence a uma categoria (por exemplo, literatura, técnico ou periódico). Cada eBook tem uma
licença de uso que define quantos alunos podem acessá-lo ao mesmo tempo, no máximo 60 acessos
simultâneos; atingido esse número, novos acessos ficam bloqueados até que uma das licenças em uso
seja liberada.

Os alunos podem adicionar à sua estante pessoal até 4 eBooks de leitura obrigatória e mais 2 de
leitura livre. Há períodos de acesso ao longo do semestre, durante os quais o aluno pode adicionar
eBooks à estante e, ou remover eBooks adicionados anteriormente. Um eBook só permanece no catálogo
licenciado no semestre seguinte se, ao final do período de acesso, tiver sido adicionado à estante
de pelo menos 3 alunos.

Sempre que um aluno adiciona um eBook à sua estante, o sistema de estatísticas de uso é notificado
pelo sistema de gestão de eBooks. Os bibliotecários podem acessar o sistema para saber quais
alunos têm determinado eBook em sua estante. Todos os usuários do sistema têm senhas, utilizadas
para validação do respectivo login.

## Documentação

- Diagrama de casos de uso: [docs/diagramas/casos-de-uso.puml](docs/diagramas/casos-de-uso.puml) ([imagem](docs/diagramas/DiagramaCasoDeUso.jpeg))
- Histórias de usuário: [docs/historias-de-usuario.md](docs/historias-de-usuario.md)
- Contribuições semanais: [docs/contribuicoes/](docs/contribuicoes/)

![Diagrama de casos de uso](docs/diagramas/DiagramaCasoDeUso.jpeg)

## Distribuição de tarefas

| Integrante | Casos de uso e histórias sob responsabilidade |
| --- | --- |
| Filipe Melo | 01 Realizar login, 02 Adicionar eBook à estante, 03 Remover eBook da estante, 04 Acessar eBook |
| João Victor | 05 Cadastrar eBook, 06 Consultar alunos com um eBook, 07 Registrar estatística de uso, 08 Renovar catálogo do semestre |

## Nota de transparência sobre uso de IA

Este projeto utilizou a ferramenta Claude, da empresa Anthropic, como apoio na documentação e na
revisão dos diagramas. O conteúdo foi revisado pelos integrantes antes do commit, e o registro por
integrante e por semana está em [docs/contribuicoes/](docs/contribuicoes/).
