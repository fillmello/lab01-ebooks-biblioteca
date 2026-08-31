# Sistema de Gestão de eBooks da Biblioteca Universitária

LAB01, Projeto I — Laboratório de Desenvolvimento de Software.

Repositório: <https://github.com/fillmello/lab01-ebooks-biblioteca>

## Descrição do sistema

Sistema para gerenciar o acervo de livros digitais (eBooks) de uma biblioteca universitária. Os
bibliotecários cadastram e mantêm o catálogo licenciado a cada semestre, e os alunos montam uma
estante pessoal com os títulos que vão usar no período, respeitando os limites das licenças de uso.

O sistema também registra as estatísticas de uso do acervo, que impactam diretamente na renovação das licenças
no semestre seguinte, e permite aos bibliotecários acompanhar quais alunos estão com cada título.

## Sprint 1 (Lab01S01) — Modelo de Análise

- Diagrama de casos de uso: [casos-de-uso.puml](docs/diagramas/casos-de-uso.puml) ([imagem](docs/diagramas/DiagramaCasoUsoPuml.png))
- Histórias de usuário: [docs/historias-de-usuario.md](docs/historias-de-usuario.md)

![Diagrama de casos de uso](docs/diagramas/DiagramaCasoUsoPuml.png)

A modelagem geral foi feita primeiro no Astah
([DiagramaCasoUsoAstah.jpeg](docs/diagramas/DiagramaCasoUsoAstah.jpeg)), para discussão dos casos de
uso, e depois transformada em PlantUML. O diagrama do Astah não reflete os ajustes feitos depois da
conversão: a numeração dos casos de uso e o ator "Usuário do Sistema" foram decisões tomadas já na
etapa do PlantUML, que é a versão vigente.

## Sprint 2 (Lab01S02) — Projeto Estrutural

- Diagrama de classes: [diagrama-de-classes.puml](docs/diagramas/diagrama-de-classes.puml) ([imagem](docs/diagramas/DiagramaClassesPuml.png))
- Projeto Java: [src/br/edu/pucminas/biblioteca/modelo/](src/br/edu/pucminas/biblioteca/modelo/)

![Diagrama de classes](docs/diagramas/DiagramaClassesPuml.png)

Cada classe é rastreável a um caso de uso da Sprint 1. Os stubs criados nesta sprint foram
implementados na Sprint 3, e o diagrama foi atualizado para refletir o código entregue.

## Sprint 3 (Lab01S03) — Protótipo funcional

Protótipo executável de ponta a ponta: interface de linha de comando, regras de negócio do enunciado
e persistência em arquivo texto.

- Interface: [MenuPrincipal.java](src/br/edu/pucminas/biblioteca/MenuPrincipal.java)
- Persistência: [BibliotecaRepositorioArquivo.java](src/br/edu/pucminas/biblioteca/persistencia/BibliotecaRepositorioArquivo.java)
- Comparação entre o que foi modelado e o que foi implementado: [docs/comparacao-modelo-implementacao.md](docs/comparacao-modelo-implementacao.md)

### Como compilar e executar

```bash
mkdir -p bin
javac -d bin $(find src -name "*.java")
java -cp bin br.edu.pucminas.biblioteca.MenuPrincipal
```

Na primeira execução o sistema cria a pasta `dados/` com um catálogo e usuários de exemplo. A pasta
não é versionada, então um clone limpo sempre começa com os mesmos dados. Todos os usuários de
exemplo usam a senha `123`:

| Login | Perfil |
| --- | --- |
| `carla` | Bibliotecária |
| `bruno`, `ana`, `lucas`, `mariana` | Alunos |

Quem ainda não tem cadastro digita `novo` na tela de login e cria a própria conta de aluno,
escolhendo id, nome, matrícula e senha. O sistema recusa id ou matrícula já usados e já deixa o
aluno logado. Contas de bibliotecário não são criadas por aí: pelo enunciado, é a equipe da
biblioteca que mantém as informações sobre os bibliotecários e os alunos.

### Funcionalidades implementadas

| Caso de uso | Onde aparece no menu | Regra aplicada |
| --- | --- | --- |
| UC01 Realizar login | Tela de login | Senha validada em `Usuario.autenticar`; a mesma tela cria a conta de aluno que ainda não existe |
| UC02 Adicionar eBook à estante | Menu do aluno, opção 2 | Até 4 obrigatórios e 2 livres, só com o período de acesso aberto |
| UC03 Remover eBook da estante | Menu do aluno, opção 3 | Só com o período de acesso aberto; a vaga do tipo volta a ficar livre |
| UC04 Acessar eBook | Menu do aluno, opções 5 e 6 | Bloqueia ao atingir o limite de acessos simultâneos da licença (máximo 60) |
| UC05 Cadastrar eBook | Menu do bibliotecário, opção 1 | Exige título, editora, formato e categoria, e recusa título repetido |
| UC06 Consultar alunos com um eBook | Menu do bibliotecário, opção 4 | Lista os alunos que têm o título na estante |
| UC07 Registrar estatística de uso | Menu do bibliotecário, opção 5 | Toda adição à estante notifica `SistemaEstatisticas` |
| UC08 Renovar catálogo do semestre | Menu do bibliotecário, opção 6 | Mantém apenas os títulos presentes em 3 ou mais estantes |

### Persistência

Os dados ficam em arquivos texto na pasta `dados/`, com campos separados por ponto e vírgula:

| Arquivo | Conteúdo |
| --- | --- |
| `catalogo.txt` | Semestre e períodos de acesso |
| `ebooks.txt` | Título, editora, formato, categoria e limite da licença |
| `usuarios.txt` | Perfil, id, nome, senha, matrícula ou registro funcional |
| `estantes.txt` | Matrícula, título, tipo de leitura e data de adição |
| `estatisticas.txt` | Título e total de adições a estantes |

## Distribuição de tarefas

### Sprint 1, casos de uso e histórias

As histórias de usuário foram levantadas em conjunto, em aula. Os casos de uso foram divididos:

| Integrante | Casos de uso e histórias sob responsabilidade |
| --- | --- |
| Filipe Melo | 01 Realizar login, 02 Adicionar eBook à estante, 03 Remover eBook da estante, 04 Acessar eBook |
| João Victor | 05 Cadastrar eBook, 06 Consultar alunos com um eBook, 07 Registrar estatística de uso, 08 Renovar catálogo do semestre |

### Sprint 2, agregações de classes

Mantida a mesma divisão da Sprint 1:

| Integrante | Agregação | Classes |
| --- | --- | --- |
| Filipe Melo | `ItemEstante o-- EBook` | Usuario, Aluno, Estante, ItemEstante, Licenca, TipoLeitura |
| João Victor | `Catalogo o-- EBook` | Bibliotecario, EBook, Catalogo, PeriodoAcesso, SistemaEstatisticas, Formato, Categoria |

### Sprint 3, funcionalidades implementadas

Mantida a mesma divisão das sprints anteriores:

| Integrante | Funcionalidades | Classes implementadas |
| --- | --- | --- |
| Filipe Melo | 01 Realizar login, 02 Adicionar eBook à estante, 03 Remover eBook da estante, 04 Acessar eBook | Usuario, Aluno, Estante, Licenca, login e menu do aluno em MenuPrincipal, parte de estantes e usuários em BibliotecaRepositorioArquivo |
| João Victor | 05 Cadastrar eBook, 06 Consultar alunos com um eBook, 07 Registrar estatística de uso, 08 Renovar catálogo do semestre | Bibliotecario, EBook, Catalogo, PeriodoAcesso, SistemaEstatisticas, menu do bibliotecário em MenuPrincipal, parte de catálogo e estatísticas em BibliotecaRepositorioArquivo |

O registro semanal de cada integrante fica em [docs/contribuicoes/](docs/contribuicoes/), um arquivo
por sprint.

## Nota de transparência sobre uso de IA

Este projeto utilizou a ferramenta Claude, da empresa Anthropic, em conformidade com a política de
uso responsável de IA da disciplina. O uso foi diferente em cada sprint:

| Sprint | Como a ferramenta foi usada |
| --- | --- |
| 1 e 2 | Revisão de texto, da consistência entre os diagramas e as histórias, e dos nomes de métodos |
| 3 | Além da revisão, geração de código nos pontos descritos abaixo |

Na Sprint 3, os trechos abaixo foram **gerados com apoio do Claude**, e não escritos do zero pelos
integrantes:

- [DadosIniciais.java](src/br/edu/pucminas/biblioteca/DadosIniciais.java) — a classe inteira. São os
  dados de exemplo criados na primeira execução (usuários e catálogo de demonstração). Não há regra
  de negócio aqui: é carga de teste, para o protótipo abrir já com conteúdo para demonstrar.
- [MenuPrincipal.java](src/br/edu/pucminas/biblioteca/MenuPrincipal.java) — a ferramenta ajudou na
  estrutura da interface de linha de comando: o laço de menu, a leitura e validação da entrada do
  usuário e o tratamento das exceções. As regras de negócio chamadas por esse menu estão nas classes
  do pacote `modelo` e foram decididas pelos integrantes.

O restante do código e todas as decisões de modelagem são dos integrantes, que revisaram cada
trecho antes do commit. O registro por integrante e por semana está em
[docs/contribuicoes/](docs/contribuicoes/).
