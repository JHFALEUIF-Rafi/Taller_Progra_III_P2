import java.time.Year;
import java.util.ArrayList;

public class CatalogoPeliculas {
    public ArrayList<Pelicula>  peliculas = new ArrayList<>();

    // R1: las veinte películas vienen desordenadas desde DatosIniciales
    public void precargar(){
        peliculas = DatosIniciales.cargar();
    }

    public CatalogoPeliculas() {
        precargar();
        ordenar();
    }

    // Burbuja por código ascendente: compara vecinos j y j+1
    public void ordenar(){
        Pelicula aux;
        for(int i=0;i<peliculas.size()-1;i++){
            for(int j=0;j<peliculas.size()-1-i;j++){
                if(peliculas.get(j).getCodigo()>peliculas.get(j+1).getCodigo()){
                    aux=peliculas.get(j);
                    peliculas.set(j,peliculas.get(j+1));
                    peliculas.set(j+1,aux);
                }
            }
        }

    }

    // R3: búsqueda lineal por título o director. Devuelve un ArrayList nuevo
    // (vacío si no hay coincidencias) en orden por código.
    public ArrayList<Pelicula> buscarCoincidencias(String texto) throws Exception {
        if (texto == null || texto.trim().isEmpty())
            throw new Exception("Ingrese un texto para buscar");

        String criterio = texto.trim().toLowerCase();
        ArrayList<Pelicula> resultados = new ArrayList<>();

        for (Pelicula p : peliculas) {
            // Con || la película se agrega una sola vez aunque coincida en ambos campos
            if (p.getTitulo().toLowerCase().contains(criterio)
                    || p.getDirector().toLowerCase().contains(criterio)) {
                resultados.add(p);
            }
        }
        return resultados;
    }

    // R5: incrementa en uno el contador de la película con ese código.
    public void darMeGusta(int codigo) throws Exception {
        if (codigo <= 0)
            throw new Exception("Seleccione una película válida");

        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1)
            throw new Exception("No existe una película con el código " + codigo);

        peliculas.get(indice).incrementarMeGusta();
    }

    // R6: copia de la lista principal ordenada por me gusta descendente con burbuja.
    // Solo se intercambia cuando el de la izquierda es MENOR, así los empates
    // conservan el orden por código (burbuja estable). La lista principal no cambia.
    public ArrayList<Pelicula> obtenerRanking() {
        ArrayList<Pelicula> ranking = new ArrayList<>(peliculas);
        Pelicula aux;
        for (int i = 0; i < ranking.size() - 1; i++) {
            for (int j = 0; j < ranking.size() - 1 - i; j++) {
                if (ranking.get(j).getMeGusta() < ranking.get(j + 1).getMeGusta()) {
                    aux = ranking.get(j);
                    ranking.set(j, ranking.get(j + 1));
                    ranking.set(j + 1, aux);
                }
            }
        }
        return ranking;
    }
    // El año va desde 1 hasta el año actual
    public static final int ANIO_MIN = 1;

    public static final int ANIO_MAX = Year.now().getValue();

    public static final String[] GENEROS = {"Ciencia ficción", "Drama", "Aventura",

            "Comedia", "Animación", "Suspenso", "Documental"};

    public void registrar(Pelicula dato) throws Exception {

        if (dato == null)
            throw new Exception("La película no puede ser nula");

        if (dato.getCodigo() <= 0)
            throw new Exception("El código debe ser positivo");

        if (dato.getTitulo() == null || dato.getTitulo().trim().isEmpty())
            throw new Exception("El título es obligatorio");

        if (dato.getDirector() == null || dato.getDirector().trim().isEmpty())
            throw new Exception("El director es obligatorio");

        if (!generoValido(dato.getGenero()))
            throw new Exception("Seleccione un género válido");

        if (dato.getAnio() < ANIO_MIN || dato.getAnio() > ANIO_MAX)
            throw new Exception("El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX);

        if (buscarIndicePorCodigo(dato.getCodigo()) != -1)
            throw new Exception("Ya existe una película con el código " + dato.getCodigo());

        peliculas.add(dato);
        ordenar();
    }

    private boolean generoValido(String genero) {
        if (genero == null) return false;
        for (String g : GENEROS) {
            if (g.equals(genero)) return true;
        }

        return false;

    }

    public int getTotal() {
        return peliculas.size();

    }
    // R4: búsqueda binaria; la lista principal siempre está ordenada por código
    public int buscarIndicePorCodigo(int codigo) throws Exception {
        if (codigo <= 0)
            throw new Exception("El código debe ser positivo");

        int inicio = 0;
        int fin = peliculas.size() - 1;

        while (inicio <= fin) {
            int medio = inicio + (fin - inicio) / 2;
            int codigoActual = peliculas.get(medio).getCodigo();

            if (codigoActual == codigo) {
                return medio;
            } else if (codigoActual < codigo) {
                inicio = medio + 1;
            } else {
                fin = medio - 1;
            }
        }
        return -1;
    }

    public void editarPelicula(int codigo, String titulo, String director, String genero, int anio) throws Exception {
        int indice = buscarIndicePorCodigo(codigo);
        if (indice == -1) {
            throw new Exception("No existe una película con el código " + codigo);
        }
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new Exception("El título es obligatorio");
        }
        if (director == null || director.trim().isEmpty()) {
            throw new Exception("El director es obligatorio");
        }
        if (!generoValido(genero)) {
            throw new Exception("Seleccione un género válido");
        }
        if (anio < ANIO_MIN || anio > ANIO_MAX) {
            throw new Exception("El año debe estar entre " + ANIO_MIN + " y " + ANIO_MAX);
        }

        Pelicula p = peliculas.get(indice);
        p.setTitulo(titulo.trim());
        p.setDirector(director.trim());
        p.setGenero(genero.trim());
        p.setAnio(anio);
    }
}
