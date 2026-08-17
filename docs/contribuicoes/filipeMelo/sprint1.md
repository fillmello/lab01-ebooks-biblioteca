# Contribuições, Sprint 1 (Lab01S01), Filipe Melo

## Semana 1 (10/08/2026)

**Contribuição:** criei o repositório `lab01-ebooks-biblioteca` no GitHub com README e `.gitignore`
de Java, montei a estrutura de pastas do projeto (`docs/diagramas/`, `docs/contribuicoes/`) e fiz o
commit inicial (`chore: cria estrutura inicial de pastas do projeto`). Configurei também a proteção
da branch `main` via ruleset do GitHub.

**Decisões:**

- Protegi a `main` com *Restrict deletions*, *Block force pushes* e *Require a pull request before
  merging*, em vez de deixar todos commitando direto. Como o diagrama e o arquivo de histórias são
  compartilhados pelos dois integrantes, o PR evita sobrescrita de trabalho, que é o erro comum
  apontado no roteiro.
- Deixei *Require signed commits* e *Require status checks* desativados: exigiriam assinatura GPG e
  CI configurado, atrito sem ganho para um grupo de 2 pessoas nesta fase do projeto.
- Adotei uma branch por integrante (`filipeMelo`, `Joao`) para trabalharmos em paralelo.

## Semana 2 (17/08/2026)

**Contribuição:** modelei o Diagrama de Casos de Uso em PlantUML
(`docs/diagramas/casos-de-uso.puml`), com os 8 casos de uso do sistema, e escrevi as histórias de
usuário correspondentes em `docs/historias-de-usuario.md`. Sou responsável especificamente por
**UC01/HU01 (Realizar login)**, **UC02/HU02 (Adicionar eBook à estante)**, **UC03/HU03 (Remover
eBook da estante)** e **UC04/HU04 (Acessar eBook)**. Também exportei a imagem do diagrama e
atualizei o README com a descrição do sistema e os links da documentação.

**Decisões:**

- Modelei "Registrar estatística de uso" como `<<include>>` de "Adicionar eBook à estante", e não
  como `<<extend>>`, porque a descrição diz "sempre que um aluno adiciona um eBook": é um fluxo
  obrigatório, não condicional.
- Modelei "Realizar login" como caso de uso associado diretamente aos dois atores, em vez de
  incluí-lo em cada um dos demais casos de uso. Como todos os usuários têm senha, o login é
  pré-condição geral do sistema; ligar `<<include>>` de todos os casos de uso para ele deixaria o
  diagrama poluído sem acrescentar informação.
- Separei "Adicionar eBook à estante" de "Remover eBook da estante" como dois casos de uso, em vez
  de um único "Gerenciar estante", para facilitar a distribuição individual de tarefas e porque as
  regras de negócio são distintas (limite de 4+2 na adição).
- Não modelei "Validar senha no banco" como caso de uso: é detalhe de implementação de "Realizar
  login", não intenção do ator.
- Escrevi as histórias com personas nomeadas (Ana, Bruno, Carla) e papel explícito, mantendo cada
  persona sempre no mesmo papel, para não confundir quem é o ator de cada história.

**Uso de IA:** usei o Claude (Anthropic) para revisar a consistência entre o diagrama e as
histórias de usuário, para gerar a estrutura inicial dos arquivos de documentação e para revisar a
redação do README. Revisei todo o conteúdo antes de commitar e ajustei manualmente o diagrama e a
nomenclatura dos casos de uso até ficarem como eu queria.
