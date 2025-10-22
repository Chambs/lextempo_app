# lextempo_app
Android app in Kotlin for automated criminal sentence calculations — helping lawyers and citizens estimate penalties, progressions, and release dates with an intuitive and modern interface.

## Desenvolvimento

- O build `debug` agora gera automaticamente um `applicationId` exclusivo por diretório do projeto (ex.: `com.lextempo.calculadorapenal.dev3fa21b`).
  Isso evita conflitos com pacotes antigos que possam estar pendentes no dispositivo e que causavam o erro `INSTALL_FAILED_DUPLICATE_PACKAGE` ao instalar pelo Android Studio.
  Caso você clone o projeto em outro local, um novo sufixo será calculado automaticamente, permitindo instalar múltiplas variantes lado a lado sem precisar limpar manualmente o dispositivo.
