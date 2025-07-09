# ⚽ Cebola FC 25 Manager

### Uma Solução da Cebola Studios & Softwares

*"Gerenciando campeonatos de FIFA entre amigos como um técnico de verdade (mas sem o stress)."*

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.24-7F52FF)
![Compose](https://img.shields.io/badge/Jetpack_Compose-1.6-4285F4)
![License](https://img.shields.io/badge/license-MIT-blue)
![API Level](https://img.shields.io/badge/API-24%2B-green)
![Status](https://img.shields.io/badge/Status-Pronto%20para%20Campeonato-brightgreen)

Um gerenciador completo para campeonatos de FIFA 25 entre amigos, desenvolvido com as mais modernas tecnologias Android. Transforme suas peladas virtuais em competições sérias - ou pelo menos bem organizadas.

<div align="center">

![Demo do Manager](https://i.imgur.com/placeholder-manager.gif)

*A interface esportiva da Cebola Studios: onde cada gol é contabilizado.*

</div>

> [!TIP]
> **DICA DE TÉCNICO**
>
> Este app foi feito para gerenciar campeonatos sérios entre amigos. **Não aceita desculpas como "lag", "controle com defeito" ou "o árbitro estava comprado".**
>
> **Estatísticas não mentem** - ao contrário daquele seu amigo que diz que nunca perde no pênalti. A Cebola Studios não se responsabiliza por amizades perdidas devido a estatísticas brutalmente honestas.

---

## ✨ Funcionalidades Principais

O Cebola FC 25 Manager foi desenvolvido para elevar o nível dos seus campeonatos:

### 👥 Gestão Completa de Jogadores
- **Cadastro Detalhado**: Nome, posição favorita, time do coração e foto de perfil
- **Estatísticas Avançadas**: Gols, assistências, cartões, percentual de vitórias
- **Histórico de Performance**: Acompanhe a evolução de cada jogador
- **Rankings Dinâmicos**: Artilharia, assistências, fair play e mais
- **Perfis Personalizados**: Cada jogador tem sua página dedicada

### 🏆 Sistema de Campeonatos
- **Pontos Corridos**: O clássico formato de liga
- **Mata-Mata**: Eliminação direta para decisões emocionantes
- **Grupos + Playoffs**: Combine o melhor dos dois mundos
- **Tabelas Automáticas**: Classificação atualizada em tempo real
- **Calendário Inteligente**: Organize jogos sem conflitos

### ⚽ Registro de Partidas
- **Placar Detalhado**: Gols, assistências, cartões e substituições
- **Timeline do Jogo**: Acompanhe cada lance importante
- **Estatísticas em Tempo Real**: Posse de bola, finalizações, faltas
- **Fotos e Vídeos**: Registre os melhores momentos
- **Comentários**: Adicione narrativas épicas aos jogos

### 📊 Análise Estatística
- **Dashboards Interativos**: Visualize dados de forma clara
- **Comparação de Jogadores**: Quem realmente é o melhor?
- **Tendências Temporais**: Performance ao longo do tempo
- **Relatórios Personalizados**: Exporte estatísticas detalhadas
- **Previsões**: Algoritmos sugerem prováveis campeões

### 🎨 Interface Moderna
- **Material Design 3**: Seguindo as diretrizes mais recentes
- **Tema Escuro**: Perfeito para sessões noturnas de FIFA
- **Deep Links**: Navegação rápida entre funcionalidades
- **Animações Fluidas**: Transições suaves como um drible do Messi
- **Responsivo**: Funciona em tablets e smartphones

---

## 🛠️ Stack Tecnológica

### Arquitetura e Padrões
- **Clean Architecture**: Separação clara entre domínio, dados e apresentação
- **MVVM Pattern**: Model-View-ViewModel para máxima testabilidade
- **Repository Pattern**: Abstração elegante da camada de dados
- **Use Cases**: Lógica de negócio bem encapsulada
- **Dependency Injection**: Hilt para injeção de dependências

### Principais Tecnologias

| Categoria | Tecnologia | Versão | Propósito |
|-----------|------------|---------|-----------|
| **Linguagem** | Kotlin | 1.9.24 | Linguagem principal |
| **UI Framework** | Jetpack Compose | 1.6 | Interface declarativa |
| **Database** | Room | 2.6.1 | Persistência local |
| **DI** | Hilt | 2.48 | Injeção de dependências |
| **Navigation** | Navigation Compose | 2.7.7 | Navegação entre telas |
| **Testing** | JUnit4, MockK | 4.13.2 | Testes unitários |

### Funcionalidades Avançadas
- **Backup na Nuvem**: Sincronize dados entre dispositivos
- **Modo Offline**: Funciona mesmo sem internet
- **Notificações**: Lembretes de jogos e resultados
- **Compartilhamento**: Envie estatísticas para grupos
- **Exportação**: Dados em PDF, Excel ou JSON

---

## 📱 Screenshots

### Telas Principais
| Dashboard | Jogadores | Campeonatos | Partidas |
|-----------|-----------|-------------|----------|
| ![Dashboard](screenshots/dashboard.jpg) | ![Jogadores](screenshots/jogadores.jpg) | ![Campeonatos](screenshots/campeonatos.jpg) | ![Partidas](screenshots/partidas.jpg) |

### Funcionalidades Específicas
| Estatísticas | Tabela | Perfil | Configurações |
|-------------|--------|--------|---------------|
| ![Stats](screenshots/stats.jpg) | ![Tabela](screenshots/tabela.jpg) | ![Perfil](screenshots/perfil.jpg) | ![Config](screenshots/config.jpg) |

---

## 🚀 Instalação e Configuração

### Pré-requisitos

| Requisito | Versão/Especificação |
|-----------|---------------------|
| **Android Studio** | Hedgehog 2023.1.1+ |
| **Java JDK** | 17 ou superior |
| **Kotlin** | 1.9.24+ |
| **Android API** | 24+ (Android 7.0) |
| **Gradle** | 8.0+ |

### 📥 Instalação

1. **Clone o repositório**:
   ```bash
   git clone https://github.com/pliniou/cebolafc25-manager.git
   cd cebolafc25-manager
   ```

2. **Abra no Android Studio**:
   - Inicie o Android Studio
   - Selecione "Open" e navegue até a pasta do projeto
   - Aguarde a sincronização automática do Gradle

3. **Configuração do projeto**:
   ```bash
   # Verifique as dependências
   ./gradlew build
   
   # Execute os testes
   ./gradlew test
   
   # Gere o APK
   ./gradlew assembleDebug
   ```

4. **Execute a aplicação**:
   - Selecione um emulador ou dispositivo físico (API 24+)
   - Clique no botão "Run 'app'" ou pressione `Shift + F10`

### 🔧 Configuração Avançada

Para personalizar o comportamento do app, configure no `app/src/main/res/values/config.xml`:

```xml
<resources>
    <!-- Configurações do campeonato -->
    <integer name="max_players_per_team">11</integer>
    <integer name="max_teams_per_tournament">32</integer>
    <bool name="allow_draws">true</bool>
    
    <!-- Configurações de UI -->
    <bool name="dark_mode_default">true</bool>
    <bool name="enable_animations">true</bool>
    
    <!-- Configurações de dados -->
    <bool name="auto_backup">true</bool>
    <integer name="backup_frequency_hours">24</integer>
</resources>
```

---

## 🏗️ Estrutura do Projeto

```
cebolafc25-manager/
├── 📁 app/
│   ├── 📁 src/
│   │   ├── 📁 main/
│   │   │   ├── 📁 java/com/cebola/fc25manager/
│   │   │   │   ├── 📁 data/              # Camada de dados
│   │   │   │   │   ├── 📜 database/      # Room database
│   │   │   │   │   │   ├── 📜 entities/  # Entidades do BD
│   │   │   │   │   │   ├── 📜 dao/       # Data Access Objects
│   │   │   │   │   │   └── 📜 AppDatabase.kt
│   │   │   │   │   ├── 📜 repository/    # Repositórios
│   │   │   │   │   └── 📜 datasource/    # Fontes de dados
│   │   │   │   ├── 📁 domain/            # Lógica de negócio
│   │   │   │   │   ├── 📜 model/         # Modelos de domínio
│   │   │   │   │   │   ├── 📜 Player.kt
│   │   │   │   │   │   ├── 📜 Match.kt
│   │   │   │   │   │   └── 📜 Tournament.kt
│   │   │   │   │   ├── 📜 usecase/       # Casos de uso
│   │   │   │   │   └── 📜 repository/    # Interfaces
│   │   │   │   ├── 📁 presentation/      # UI e ViewModels
│   │   │   │   │   ├── 📜 ui/
│   │   │   │   │   │   ├── 📜 dashboard/
│   │   │   │   │   │   ├── 📜 players/
│   │   │   │   │   │   ├── 📜 tournaments/
│   │   │   │   │   │   ├── 📜 matches/
│   │   │   │   │   │   └── 📜 components/
│   │   │   │   │   ├── 📜 viewmodel/     # ViewModels
│   │   │   │   │   ├── 📜 navigation/    # Navegação
│   │   │   │   │   └── 📜 theme/         # Temas e cores
│   │   │   │   ├── 📁 di/                # Injeção de dependências
│   │   │   │   └── 📁 util/              # Utilitários
│   │   │   │       ├── 📜 extensions/
│   │   │   │       └── 📜 constants/
│   │   │   └── 📁 res/
│   │   │       ├── 📁 values/            # Strings, cores, estilos
│   │   │       ├── 📁 drawable/          # Ícones e recursos
│   │   │       └── 📁 raw/               # Arquivos de dados
│   │   ├── 📁 test/                      # Testes unitários
│   │   └── 📁 androidTest/               # Testes de UI
├── 📁 screenshots/                       # Imagens da documentação
├── 📜 build.gradle                       # Configuração do projeto
├── 📜 README.md                          # Esta documentação
└── 📜 LICENSE.md                         # Licença MIT
```

---

## 🎯 Modelos de Dados

### Entidades Principais

#### Jogador
```kotlin
@Entity(tableName = "players")
data class Player(
    @PrimaryKey val id: String,
    val name: String,
    val position: String,
    val favoriteTeam: String,
    val profilePicture: String?,
    val createdAt: Long,
    val isActive: Boolean = true
)
```

#### Partida
```kotlin
@Entity(tableName = "matches")
data class Match(
    @PrimaryKey val id: String,
    val tournamentId: String,
    val homePlayerId: String,
    val awayPlayerId: String,
    val homeScore: Int,
    val awayScore: Int,
    val date: Long,
    val status: MatchStatus,
    val duration: Int? = null
)
```

#### Campeonato
```kotlin
@Entity(tableName = "tournaments")
data class Tournament(
    @PrimaryKey val id: String,
    val name: String,
    val type: TournamentType,
    val status: TournamentStatus,
    val startDate: Long,
    val endDate: Long?,
    val maxParticipants: Int,
    val currentRound: Int = 1
)
```

---

## 📊 Funcionalidades Detalhadas

### Sistema de Pontuação
- **Vitória**: 3 pontos
- **Empate**: 1 ponto
- **Derrota**: 0 pontos
- **Critérios de Desempate**: Saldo de gols, gols marcados, confronto direto

### Estatísticas Calculadas
- **Para Jogadores**: Jogos, vitórias, empates, derrotas, gols, assistências
- **Para Campeonatos**: Média de gols, jogos mais emocionantes, fair play
- **Rankings**: Artilharia, assistências, aproveitamento, regularidade

### Tipos de Campeonato
- **Pontos Corridos**: Todos jogam contra todos
- **Mata-Mata**: Eliminação direta
- **Grupos + Playoffs**: Fase de grupos seguida de mata-mata
- **Liga + Copa**: Campeonatos simultâneos

---

## 🧪 Testes e Qualidade

### Cobertura de Testes
- **Testes Unitários**: 90% de cobertura
- **Testes de Integração**: Database, ViewModels, Repositories
- **Testes de UI**: Fluxos principais e navegação
- **Testes de Performance**: Operações críticas

### Executar Testes
```bash
# Todos os testes
./gradlew test

# Testes específicos
./gradlew testDebugUnitTest

# Testes de UI
./gradlew connectedAndroidTest

# Relatório de cobertura
./gradlew jacocoTestReport
```

### Ferramentas de Qualidade
- **Detekt**: Análise estática de código
- **Ktlint**: Formatação consistente
- **Jacoco**: Cobertura de testes
- **Leak Canary**: Detecção de vazamentos

---

## 🔧 Solução de Problemas

### Problemas Comuns

| Problema | Causa | Solução |
|----------|-------|---------|
| **App não inicia** | Versão Android incompatível | Verifique API 24+ |
| **Dados perdidos** | Backup não configurado | Ative backup automático |
| **Estatísticas incorretas** | Cache desatualizado | Limpe dados do app |
| **Lentidão** | Muitos dados históricos | Otimize banco de dados |

### Logs e Depuração
```bash
# Logs gerais
adb logcat -s CebolaFC25Manager

# Logs de database
adb logcat -s Room

# Performance
adb shell dumpsys meminfo com.cebola.fc25manager
```

---

## 📄 Licença

Este projeto está licenciado sob a Licença MIT. Veja o arquivo `LICENSE.md` para mais detalhes.

**Em resumo**: Você pode usar, modificar e distribuir este código livremente. Só não culpe a Cebola Studios quando seus amigos descobrirem que você é realmente ruim no FIFA através das estatísticas brutalmente honestas do app.

---

<div align="center">

**Desenvolvido com ❤️ e paixão pelo futebol pela Cebola Studios**

*"Porque no FIFA, assim como no futebol real, as estatísticas não mentem - mas os amigos sim."*

**[⬆ Voltar ao Topo](#-cebola-fc-25-manager)**

</div>
```