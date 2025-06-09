package com.br.votacao.service;

import com.br.votacao.job.EncerrarSessaoVotacaoJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuartzSchedulerService {

    private final Scheduler scheduler;

    public void agendarEncerramentoSessao(Long sessaoId, Date dataHoraFim) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(EncerrarSessaoVotacaoJob.class)
                    .withIdentity("encerrarSessaoJob-" + sessaoId, "votacao")
                    .usingJobData("sessaoId", sessaoId)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("encerrarSessaoTrigger-" + sessaoId, "votacao")
                    .startAt(dataHoraFim)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Job Quartz agendado para encerrar sessão {} em {}", sessaoId, dataHoraFim);

        } catch (SchedulerException e) {
            log.error("Erro ao agendar encerramento da sessão de votação", e);
        }
    }
}
