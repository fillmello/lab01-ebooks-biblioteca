# Histórias de Usuário
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

Critérios de aceitação:
- Senha correta concede acesso ao sistema.
- Senha incorreta bloqueia o acesso e informa o erro.
- Vale para os dois perfis de usuário, aluno e bibliotecário.

## HU02 — Adicionar eBook à estante

Como Bruno, aluno, eu quero adicionar à minha estante pessoal até 4 eBooks de leitura obrigatória
e 2 de leitura livre, para que eu possa acessá-los durante o semestre.

Critérios de aceitação:
- Permite adicionar até 4 eBooks de leitura obrigatória e até 2 de leitura livre.
- Bloqueia a adição do 5º eBook obrigatório e do 3º de leitura livre.
- Só permite adicionar durante o período de acesso.
- Cada adição registra uma estatística de uso.

## HU03 — Remover eBook da estante

Como Bruno, aluno, eu quero remover eBooks adicionados anteriormente à minha estante, para que eu
possa ajustar minha estante durante o período de acesso.

Critérios de aceitação:
- O eBook removido deixa de constar na estante do aluno.
- A vaga do tipo correspondente, obrigatória ou livre, fica disponível para uma nova adição.
- Só permite remover durante o período de acesso.

## HU04 — Acessar eBook

Como Bruno, aluno, eu quero acessar um eBook da minha estante, para que eu possa lê-lo enquanto
houver licença de uso disponível, respeitado o limite de 60 acessos simultâneos.

Critérios de aceitação:
- Concede o acesso enquanto houver menos de 60 acessos simultâneos ao mesmo eBook.
- Ao atingir 60 acessos simultâneos, bloqueia novos acessos àquele eBook.
- Libera um novo acesso assim que uma das licenças em uso é liberada.

## HU05 — Cadastrar eBook

Como Carla, bibliotecária, eu quero cadastrar os eBooks disponíveis a cada semestre, com título,
editora, formato e categoria, para que os alunos possam adicioná-los à sua estante.

Critérios de aceitação:
- Exige título, editora, formato de arquivo e categoria.
- Recusa o cadastro se algum desses dados não for informado.
- Após o cadastro, o eBook fica disponível no catálogo do semestre.

## HU06 — Consultar alunos com um eBook

Como Carla, bibliotecária, eu quero saber quais alunos têm determinado eBook em sua estante, para
que eu possa acompanhar o uso do acervo.

Critérios de aceitação:
- Retorna a lista de alunos que têm o eBook consultado na estante.
- Retorna lista vazia quando nenhum aluno tem aquele eBook.

## HU07 — Registrar estatística de uso

Como Carla, bibliotecária, eu quero que o sistema de estatísticas de uso seja notificado sempre
que um aluno adiciona um eBook à estante, para que a biblioteca possa acompanhar quais títulos são
mais utilizados pelos alunos.

Critérios de aceitação:
- Toda adição de eBook à estante registra uma estatística de uso.
- Remover ou acessar um eBook não registra estatística.

## HU08 — Renovar catálogo do semestre

Como Carla, bibliotecária, eu quero manter no catálogo licenciado apenas os eBooks adicionados à
estante de pelo menos 3 alunos ao final do período de acesso, para que os títulos pouco utilizados
tenham a licença não renovada e sejam removidos.

Critérios de aceitação:
- Ao final do período de acesso, eBooks presentes na estante de 3 ou mais alunos permanecem no
  catálogo do semestre seguinte.
- eBooks presentes na estante de menos de 3 alunos têm a licença não renovada e saem do catálogo.
