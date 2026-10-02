package Cliente;
//primero el modelo de negocio, MAVEN controla las operaciones
//() declaracion
public record Cliente(String nombre, String email, String dni) {

    public Cliente{
        if(nombre==null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre no puede estar vacio");//Is Blank:vacio / Nullo Abstract / Vacio Datos+estructura
        }
        if(email==null || !email.contains("@")){//programar de manera positiva
            throw new IllegalArgumentException("El email no es valido");
        }
        if(dni==null|| !dni.matches("\\d{8}")){
            throw new IllegalArgumentException("DNI debe tener 8 digitos numéricos");

        }
    }

}
