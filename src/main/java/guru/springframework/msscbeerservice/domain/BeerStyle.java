package guru.springframework.msscbeerservice.domain;

public enum BeerStyle {
    LAGER("LAGER"), // 1
    PILSENER("PILSENER"), // 2
    STOUT("STOUT"), // 3
    GOSE("GOSE"), // 4
    PORTER("PORTER"), // 5
    ALE("ALE"), // 6
    WHEAT("WHEAT"), // 7
    IPA("IPA"), // 8
    PALE_ALE("PALE_ALE"), // 9
    SAISON("SAISON"), // 10
    HONEY("HONEY"); // 11

    public final String label;

    private BeerStyle(String label) {
        this.label = label;
    }
}
