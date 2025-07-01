# 🧅 Cebola FC 25 Manager

[![Licença](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE.md)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-7F52FF.svg?logo=kotlin)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6-4285F4.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)

Um aplicativo Android moderno para gerenciar campeonatos locais de EA FC 25 (antigo FIFA) e acompanhar estatísticas de partidas entre amigos, construído com as melhores práticas de desenvolvimento Android.

## 🖼️ Telas da Aplicação

| Tela de Partidas | Tela de Estatísticas | Tela de Jogadores |
|------------------|----------------------|-------------------|
|                  |                      |                   |

| Tela de Detalhes do Jogador | Tela de Campeonatos (Dark Mode) | Formulário de Partida (Dark Mode) |
|-----------------------------|---------------------------------|-----------------------------------|
|                             |                                 |                                   |


## ✨ Funcionalidades Principais

-   **👤 Gestão de Jogadores:** Cadastre, liste e visualize detalhes de cada jogador.
-   **⚽ Registro de Partidas:** Adicione resultados de partidas, incluindo jogadores, times utilizados e placares.
-   **🏆 Gestão de Campeonatos:** Crie campeonatos de diferentes formatos (Pontos Corridos, Mata-mata, etc.).
-   **📊 Tabela de Classificação:** Visualize uma tabela de classificação geral em tempo real com pontos, jogos, vitórias, empates, derrotas, saldo de gols e gols pró.
-   **📱 UI Moderna e Responsiva:** Interface construída com Jetpack Compose e Material Design 3, com suporte a **Dark Mode** e adaptável a diferentes tamanhos de tela.
-   **🔗 Deep Links:** Acesse diretamente a tela de detalhes de um jogador através de um URI (ex: `cebolafc://player/{id}`).

## 🛠️ Stack Tecnológica e Arquitetura

Este projeto foi desenvolvido seguindo as diretrizes da **Clean Architecture** para garantir um código desacoplado, testável e escalável.

-   **Linguagem:** [Kotlin](https://kotlinlang.org/)
-   **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) com [Material Design 3](https://m3.material.io/)
-   **Arquitetura:**
    -   **Clean Architecture** (camadas de UI, Domínio e Dados)
    -   **MVVM** (Model-View-ViewModel)
    -   **Padrão Repository**
    -   **UseCases** para encapsular a lógica de negócio
    -   Princípios **SOLID**
-   **Injeção de Dependência:** [Hilt](https://dagger.dev/hilt/)
-   **Navegação:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
-   **Assincronismo:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) e [Flow](https://kotlinlang.org/docs/flow.html)
-   **Persistência de Dados:** [Room](https://developer.android.com/training/data-storage/room)
-   **Testes:**
    -   **Unitários:** [JUnit 4](https://junit.org/junit4/), [MockK](https://mockk.io/)
    -   **UI:** [Espresso](https://developer.android.com/training/testing/espresso), [UI Automator](https://developer.android.com/training/testing/ui-automator)

### Diagrama da Arquitetura

```mermaid
graph TD
    subgraph UI Layer
        A[Screens (Compose)] --> B[ViewModel (Hilt)]
    end

    subgraph Domain Layer
        C[Use Cases]
    end

    subgraph Data Layer
        D[Repositories] --> E[Room (Local DB)]
        D --> F[Retrofit (Remote API - Futuro)]
    end

    B --> C
    C --> D
```

## 🚀 Como Executar o Projeto

Para compilar e executar o projeto, siga os passos abaixo:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/cebolafc25-manager.git
    ```

2.  **Abra no Android Studio:**
    -   Abra o [Android Studio](https://developer.android.com/studio) (versão Hedgehog ou mais recente é recomendada).
    -   Selecione `Open` e navegue até a pasta do projeto clonado.

3.  **Sincronize as dependências:**
    -   O Gradle irá sincronizar e baixar todas as dependências necessárias automaticamente.

4.  **Execute a aplicação:**
    -   Selecione um emulador ou um dispositivo físico (API 24+).
    -   Clique no botão `Run 'app'`.

## 📈 Principais Refatorações e Melhorias

Este projeto passou por um processo de refatoração focado em qualidade, performance e manutenibilidade.

-   **Pureza Arquitetural:** Lógica de negócio movida dos ViewModels para `UseCases` dedicados, aderindo ao Princípio de Responsabilidade Única (SRP).
-   **Segurança de Ciclo de Vida:** Substituição da coleta de `Flows` em blocos `init` por `collectAsStateWithLifecycle` na UI, prevenindo memory leaks.
-   **Otimização de Performance:** Adição da dependência de `Baseline Profiles` para acelerar a inicialização do app e reduzir o jank.
-   **Integridade do Banco de Dados:** Uso de `@Transaction` no Room para garantir operações atômicas e seguras.
-   **Segurança de Rede:** Implementação de um `network-security-config` para forçar conexões HTTPS.
-   **Acessibilidade (a11y):** Adição de `contentDescription` em elementos visuais para compatibilidade com leitores de tela.

## 🗺️ Roadmap e Futuras Melhorias

-   [ ] **Modularização:** Separar cada funcionalidade (Partidas, Jogadores, etc.) em módulos de features.
-   [ ] **Testes de UI:** Adicionar testes de Snapshot com Paparazzi ou Roborazzi para garantir a consistência visual.
-   [ ] **CI/CD:** Implementar um pipeline no GitHub Actions para rodar testes e gerar builds automaticamente.
-   [ ] **Criptografia do Banco:** Adicionar criptografia com [SQLCipher](https://www.zetetic.net/sqlcipher/) para proteger os dados locais.
-   [ ] **Sincronização na Nuvem:** Implementar uma API REST para permitir que os usuários façam backup e sincronizem seus dados.

## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo [LICENSE.md](LICENSE.md) para mais detalhes.

---

Feito com ❤️ por Cebola Studios