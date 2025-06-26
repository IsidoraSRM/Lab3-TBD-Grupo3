package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.PagoEntity;
import com.Docdelivery.Backend.Repository.PagoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PagoService {

	@Autowired
	private PagoRepository pagoRepository;

	public List<PagoEntity> getAllPagos() {
		return pagoRepository.findAll();
	}

	public Optional<PagoEntity> getPagoById(Long idPago) {
		return pagoRepository.findById(idPago);
	}

	public void createPago(PagoEntity pago) {
		pagoRepository.save(pago);
	}

	public Map<String, Object> getMedioDePagoMasUsadoEnUrgentes() {
		List<Map<String, Object>> result = pagoRepository.findMedioDePagoMasUsadoEnUrgentes();
		if (result != null && !result.isEmpty()) {
			Map<String, Object> response = result.get(0);
			return response;
		} else {
			return null; // O un mapa con mensaje de error si prefieres manejarlo así.
		}
	}
}
