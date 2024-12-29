DELIMITER $$

-- Trigger para verificar quando uma tarefa é atualizada
CREATE TRIGGER `after_tarefa_update`
AFTER UPDATE ON `tarefas`
FOR EACH ROW
BEGIN
    -- Verifica se o status foi alterado para concluído
    IF NEW.status = 1 THEN
        -- Atualiza o status da demanda para 'CONCLUIDA' se todas as tarefas estiverem concluídas
        IF NOT EXISTS (
            SELECT 1 
            FROM tarefas 
            WHERE id_demanda = NEW.id_demanda AND status != 1
        ) THEN
            UPDATE demandas
            SET status = 'CONCLUIDA'
            WHERE id_demanda = NEW.id_demanda;
        END IF;
    END IF;
END$$

-- Trigger para verificar quando uma nova tarefa é inserida
CREATE TRIGGER `after_tarefa_insert`
AFTER INSERT ON `tarefas`
FOR EACH ROW
BEGIN
    -- Atualiza o status da demanda para 'EM_ANDAMENTO' se houver novas tarefas
    UPDATE demandas
    SET status = 'EM_ANDAMENTO'
    WHERE id_demanda = NEW.id_demanda;
END$$

DELIMITER ;
