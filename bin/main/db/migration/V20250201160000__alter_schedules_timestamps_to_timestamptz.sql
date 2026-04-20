-- Melhoria: converte as colunas de TIMESTAMP (sem fuso horário) para TIMESTAMPTZ
-- (timestamp with time zone), que é o tipo correto para armazenar OffsetDateTime
-- no PostgreSQL sem perda de informação de fuso horário.
ALTER TABLE SCHEDULES
    ALTER COLUMN start_at TYPE TIMESTAMPTZ USING start_at AT TIME ZONE 'UTC',
    ALTER COLUMN end_at   TYPE TIMESTAMPTZ USING end_at   AT TIME ZONE 'UTC';
