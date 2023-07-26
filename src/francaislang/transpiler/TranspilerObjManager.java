package francaislang.transpiler;

import francaislang.objects.FrancaisLangObj;
import francaislang.objects.FrancaisLangTexte;
import francaislang.objects.FrancaisLangTableau;
import org.ascore.ast.buildingBlocs.Expression;

import java.util.*;

public class TranspilerObjManager {
    /**
     * Map(PropertyName, Map(ObjTypeName, PropertyCode))
     */
    private static final HashMap<String, Map<Class<? extends FrancaisLangObj<?>>, String>> ALL_PROPERTIES = new HashMap<>();
    private static final Set<String> seenProperties = new HashSet<>();

    static {
        propertiesOf(FrancaisLangTexte.class,
                Map.entry("Taille", "@p (##rib (##field1 s) 0 FrancaisLangEntier-type)")
        );

        propertiesOf(FrancaisLangTableau.class,
                Map.entry("Taille", "@p (##rib (length (##field0 s)) 0 FrancaisLangEntier-type)"),
                Map.entry("Ajouter", "@! (##rib (append (##field0 s) (list (##field0 p))) (##+ 1 (length (##field0 s))) FrancaisLangTableau-type)")
        );
    }

    public TranspilerObjManager(TranspileCtx transpileCtx) {
        this.transpileCtx = transpileCtx;
    }

    /**
     * @param type       ObjTypeName
     * @param properties Entries of (PropertyName, PropertyCode)
     */
    @SafeVarargs
    private static void propertiesOf(Class<? extends FrancaisLangObj<?>> type, Map.Entry<String, String>... properties) {
        Arrays.stream(properties).forEach(prop -> {
            var map = ALL_PROPERTIES.putIfAbsent(prop.getKey(), new HashMap<>());
            if (map == null) map = ALL_PROPERTIES.get(prop.getKey());
            map.put(type, prop.getValue());
        });
    }

    private final TranspileCtx transpileCtx;

    List<Map.Entry<String, Class<? extends FrancaisLangObj<?>>>> registerProperties(String propertyName, Expression<?> obj) {
        var properties = new ArrayList<Map.Entry<String, Class<? extends FrancaisLangObj<?>>>>();
        for (var property : ALL_PROPERTIES.get(propertyName).entrySet()) {
            var formattedName = formatPropertyName(propertyName, property.getKey());
            var body = ALL_PROPERTIES.get(propertyName).get(property.getKey());
            var argPrefix = body.substring(0, 2);

            properties.add(Map.entry(formattedName, property.getKey()));

            if (seenProperties.add(formattedName)) {
                body = body.substring(3);

                String args = switch (argPrefix) {
                    case "@p" -> "s";
                    case "@m", "@!" -> "s p";
                    default -> throw new IllegalArgumentException("Unknown property prefix: \"" + argPrefix + "\".");
                };

                transpileCtx.appendCodeToStart("(define (%s %s) %s)\n".formatted(formattedName, args, body));
            }
        }
        return properties;
    }

    /**
     * Si on connaît le type de l'objet, on crée une fonction spécialisée nommée selon le format:<br> <code>CodeMdrType.NomPropriété</code>.
     * <br>
     * Cette fonction prend comme premier paramètre l'objet. Si la propriété est une <code>méthode</code>,
     * alors la fonction prend aussi une liste d'argument comme deuxième paramètre.
     */
    String registerProperty(String propertyName, FrancaisLangObj<?> obj) {
        var formattedName = formatPropertyName(propertyName, obj.getClass());

        if (seenProperties.add(formattedName)) {
            var body = ALL_PROPERTIES.get(propertyName).get(obj.getClass());
            var argPrefix = body.substring(0, 2);

            body = body.substring(3);

            String args = switch (argPrefix) {
                case "@p" -> "s";
                case "@m", "@!" -> "s p";
                default -> throw new IllegalArgumentException("Unknown property prefix: \"" + argPrefix + "\".");
            };

            transpileCtx.appendCodeToStart("(define (%s %s) %s)\n".formatted(formattedName, args, body));
        }
        return formattedName;
    }


    /**
     * @return <code>@p</code> if property, <code>@m</code> if method, <code>@!</code> if method that changes the caller
     */
    @SuppressWarnings("rawtypes")
    String getPropertyType(String propertyName, Class<? extends FrancaisLangObj> mdrClass) {
        return ALL_PROPERTIES.get(propertyName).get(mdrClass).substring(0, 2);
    }

    @SuppressWarnings("rawtypes")
    public static String formatPropertyName(String propertyName, Class<? extends FrancaisLangObj> mdrClass) {
        return "%s.%s".formatted(mdrClass.getSimpleName(), propertyName);
    }
}
