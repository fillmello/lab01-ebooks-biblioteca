# Contribuições, Sprint 1 (Lab01S01), Filipe Melo

## Semana 1

Contribuição: criei o repositório no GitHub com README e .gitignore de Java, montei a estrutura de
pastas do projeto e configurei a proteção da branch main.

Decisões: exigi Pull Request para alterar a main, em vez de deixar todos commitando direto, porque
o diagrama e o arquivo de histórias são compartilhados e editar direto na main geraria sobrescrita
de trabalho.

## Semana 2

Contribuição: modelei o Diagrama de Casos de Uso em PlantUML, com os 8 casos de uso do sistema, e
exportei a imagem do diagrama. Também atualizei o README com a descrição do sistema e os links da
documentação.

Decisões: modelei "Registrar estatística de uso" como caso de uso incluído (<<include>>) de
"Adicionar eBook à estante", e não como <<extend>>, porque o enunciado diz que a notificação ocorre
sempre que um aluno adiciona um eBook, ou seja, é fluxo obrigatório e não condicional.

Uso de IA: usei o Claude (Anthropic) para revisar a consistência entre o diagrama e as histórias e
para revisar a redação do README. Ajustei o diagrama manualmente antes do commit.
