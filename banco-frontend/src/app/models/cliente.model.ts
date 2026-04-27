export interface Cliente {
  id?: number;
  nombre: string;
  genero: string;
  edad: number | null;
  identificacion: string;
  direccion: string;
  telefono: string;
  password?: string;
  estado: boolean;
}