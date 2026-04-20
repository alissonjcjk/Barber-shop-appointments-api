-- Melhoria de performance: adiciona índices nas colunas mais consultadas da tabela SCHEDULES.
--
-- IDX_SCHEDULES_CLIENT_ID: necessário pois FK sem índice gera seq scan ao buscar
--   agendamentos por cliente (ex: CASCADE delete, joins).
--
-- IDX_SCHEDULES_START_END: usado pela query de listagem por mês e pela verificação
--   de conflito de horário (existsByStartAtLessThanAndEndAtGreaterThan).
CREATE INDEX IF NOT EXISTS IDX_SCHEDULES_CLIENT_ID ON SCHEDULES (client_id);
CREATE INDEX IF NOT EXISTS IDX_SCHEDULES_START_END  ON SCHEDULES (start_at, end_at);
