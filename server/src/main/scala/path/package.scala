package object path {

  opaque type Path = String

  //noinspection NoTargetNameAnnotationForOperatorLikeDefinition
  object Path {
    def here: Path = "./"

    def pwd: Path = Paths.resolve(here)

    extension (path: Path)
      def /(other: Path): Path = Paths.join(path, other)

      def :/(other: String): Path = Paths.join(path, other)
  }

}
