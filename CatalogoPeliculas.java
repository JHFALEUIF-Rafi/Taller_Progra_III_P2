import java.util.ArrayList;
import java.util.List;

public class CatalogoPeliculas {
    public ArrayList<Pelicula>  peliculas = new ArrayList<>();
    public void precargar(){
        peliculas.add(new Pelicula(2016, "Cartas de invierno", "Nicolás López", "Drama", 2025, 1));
        peliculas.add(new Pelicula(2003, "El último mapa", "Carla Ríos", "Aventura", 2022, 6));
        peliculas.add(new Pelicula(2010, "La isla del viento", "Marco Vera", "Aventura", 2020, 2));
        peliculas.add(new Pelicula(2001, "La ciudad de los faros", "Ana Torres", "Drama", 2021, 4));
        peliculas.add(new Pelicula(2018, "Vecinos por un día", "Camila Vega", "Comedia", 2023, 0));
        peliculas.add(new Pelicula(2008, "Caminos de la montaña", "Pablo Soto", "Documental", 2024, 1));
        peliculas.add(new Pelicula(2005, "Guardianes del bosque", "Sofía Paz", "Animación", 2023, 0));
        peliculas.add(new Pelicula(2013, "Señales desde el espacio", "Gabriela Ríos", "Ciencia ficción", 2023, 4));
        peliculas.add(new Pelicula(2002, "Viaje al espacio", "Luis Mora", "Ciencia ficción", 2020, 1));
        peliculas.add(new Pelicula(2019, "Amigos del espacio", "Tomás León", "Animación", 2024, 4));
        peliculas.add(new Pelicula(2006, "Sombras del puerto", "Diego Ruiz", "Suspenso", 2021, 6));
        peliculas.add(new Pelicula(2015, "Memorias del río", "Valeria Sol", "Documental", 2024, 5));
        peliculas.add(new Pelicula(2004, "Una tarde inesperada", "Pedro Vega", "Comedia", 2019, 2));
        peliculas.add(new Pelicula(2020, "El puente de cristal", "Isabel Rojas", "Suspenso", 2025, 3));
        peliculas.add(new Pelicula(2011, "Un plan diferente", "Daniela Vega", "Comedia", 2025, 6));
        peliculas.add(new Pelicula(2007, "Ecos del espacio", "Elena Ramos", "Ciencia ficción", 2020, 3));
        peliculas.add(new Pelicula(2017, "La ruta del tesoro", "Paula Reyes", "Aventura", 2021, 2));
        peliculas.add(new Pelicula(2009, "El café de la esquina", "Laura Díaz", "Drama", 2022, 5));
        peliculas.add(new Pelicula(2014, "La puerta cerrada", "Mateo Peña", "Suspenso", 2022, 3));
        peliculas.add(new Pelicula(2012, "El reloj de papel", "Andrés Luna", "Animación", 2021, 0));
    }

    public CatalogoPeliculas() {
        precargar();
        ordenar();
    }

    public void ordenar(){
        Pelicula aux;
        for(int i=0;i<peliculas.size()-1;i++){
            for(int j=i+1;j<peliculas.size();j++){
                if(peliculas.get(i).getCodigo()>peliculas.get(j).getCodigo()){
                    aux=peliculas.get(i);
                    peliculas.set(i,peliculas.get(j));
                    peliculas.set(j,aux);
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

        for (Pelicula p : peliculas) {
            if (p.getCodigo() == codigo) {
                p.incrementarMeGusta();
                return;
            }
        }
        throw new Exception("No existe una película con el código " + codigo);
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
    public static final int ANIO_MIN = 1900;

    public static final int ANIO_MAX = 2026;

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

        for (Pelicula aux : peliculas) {
            if (aux.getCodigo() == dato.getCodigo())
                throw new Exception("Ya existe una película con el código " + dato.getCodigo());

        }

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

}
