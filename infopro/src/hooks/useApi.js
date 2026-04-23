import { useState, useEffect, useCallback } from 'react';

export function useApi(fn, deps = []) {
  const [data,    setData]    = useState(null);
  const [loading, setLoading] = useState(true);
  const [error,   setError]   = useState(null);

  const fetch = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const res = await fn();
      // clientService already returns .data, not axios response wrapper
      setData(res);
    } catch (e) {
      setError(e.message || 'Une erreur est survenue');
    } finally {
      setLoading(false);
    }
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, deps);

  useEffect(() => { fetch(); }, [fetch]);
  return { data, loading, error, refetch: fetch };
}

export function useAsyncAction() {
  const [loading, setLoading] = useState(false);
  const [error,   setError]   = useState(null);

  const execute = useCallback(async (fn, onSuccess) => {
    setLoading(true);
    setError(null);
    try {
      const res = await fn();
      // clientService already returns .data, not axios response wrapper
      onSuccess && onSuccess(res);
    } catch (e) {
      setError(e.message || 'Une erreur est survenue');
    } finally {
      setLoading(false);
    }
  }, []);

  return { loading, error, execute };
}
