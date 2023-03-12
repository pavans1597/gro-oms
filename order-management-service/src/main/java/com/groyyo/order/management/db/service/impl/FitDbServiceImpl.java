package com.groyyo.order.management.db.service.impl;

import com.groyyo.core.sqlPostgresJpa.service.impl.AbstractJpaServiceImpl;
import com.groyyo.order.management.db.service.FitDbService;
import com.groyyo.order.management.entity.Fit;
import com.groyyo.order.management.repository.FitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class FitDbServiceImpl extends AbstractJpaServiceImpl<Fit, Long, FitRepository> implements FitDbService {

	@Autowired
	private FitRepository fitRepository;

	@Override
	protected FitRepository getJpaRepository() {
		return fitRepository;
	}

	@Override
	public List<Fit> getAllFits(String factoryId) {
		return (!Objects.isNull(factoryId)?
				fitRepository.findByAndFactoryIdOrderByNameAsc(factoryId)
				: fitRepository.findByOrderByNameAsc());

	}

	@Override
	public List<Fit> getAllFitsForStatus(boolean status,String factoryId) {

		return (!Objects.isNull(factoryId)?
				fitRepository.findByStatusAndFactoryIdOrderByNameAsc(status, factoryId)
				: fitRepository.findByStatusOrderByNameAsc(status));
	}

	@Override
	public Fit getFitById(String id) {
		return fitRepository.findByUuid(id);
	}

	@Override
	public Fit saveFit(Fit fit) {
		return fitRepository.saveAndFlush(fit);
	}

	@Override
	public Fit activateDeactivateFit(Fit fit, boolean status) {
		fit.setStatus(status);
		return fitRepository.saveAndFlush(fit);
	}

	@Override
	public boolean isEntityExistsByName(String name) {
		return Objects.nonNull(fitRepository.findByName(name));
	}
}
