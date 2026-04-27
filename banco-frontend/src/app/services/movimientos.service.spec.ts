import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { MovimientosService } from './movimientos.service';

describe('MovimientosService', () => {
  let service: MovimientosService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient()
      ]
    });

    service = TestBed.inject(MovimientosService);
  });

  it('debería crearse', () => {
    expect(service).toBeTruthy();
  });
});